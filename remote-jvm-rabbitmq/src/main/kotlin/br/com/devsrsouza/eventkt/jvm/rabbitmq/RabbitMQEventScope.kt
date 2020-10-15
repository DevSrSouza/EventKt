package br.com.devsrsouza.eventkt.jvm.rabbitmq

import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import br.com.devsrsouza.eventkt.remote.RemoteEventScope
import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

private typealias ConsumerTag = String
private typealias EventUniqueId = String

class RabbitMQEventScope(
    override val enconder: RemoteEncoder<ByteArray>,
    val channel: Channel
) : RemoteEventScope<ByteArray>() {
    private val consumersTag: MutableMap<EventUniqueId, ConsumerTag> = ConcurrentHashMap()
    private val subscriptionsSharedFlow: MutableMap<String, SharedFlow<Any>> = ConcurrentHashMap()

    override fun publishToRemote(value: ByteArray, eventUniqueId: String) {
        launch(Dispatchers.IO) {
            channel.queueDeclare(eventUniqueId, false, false, false, null)

            channel.basicPublish("", eventUniqueId, null, value)
        }
    }

    override fun <T : Any> listen(type: KClass<T>): Flow<T> {
        val eventUniqueId = enconder.typeUniqueId(type, listenTypes)

        val sharedFlow = subscriptionsSharedFlow[eventUniqueId]

        if(sharedFlow != null) return sharedFlow as Flow<T>

        val subscriptionFlow = MutableSharedFlow<T>(extraBufferCapacity = 1)

        subscriptionFlow.subscriptionCount
            .map { count -> count > 0 }
            .distinctUntilChanged()
            .onEach { isActive ->
                if(isActive) {
                    channel.queueDeclare(eventUniqueId, false, false, false, null)

                    val consumerTag = channel.basicConsume(eventUniqueId, true, object : DefaultConsumer(channel) {
                        override fun handleDelivery(
                            consumerTag: String,
                            envelope: Envelope,
                            properties: AMQP.BasicProperties?,
                            body: ByteArray
                        ) {
                            publishFromRemote(body)
                        }
                    })

                    insertConsumerTag(eventUniqueId, consumerTag)
                } else {
                    channel.basicCancel(consumersTag[eventUniqueId])
                }
            }
            .launchIn(this)

        super.listen(type)
            .onEach {
                subscriptionFlow.tryEmit(it)
            }
            .launchIn(this)

        subscriptionsSharedFlow[eventUniqueId] = subscriptionFlow

        return subscriptionFlow
    }

    private fun insertConsumerTag(eventUniqueId: EventUniqueId, consumerTag: ConsumerTag) {
        consumersTag[eventUniqueId] = consumerTag
    }
}
