package br.com.devsrsouza.eventkt.jvm.rabbitmq

import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import br.com.devsrsouza.eventkt.remote.RemoteEventScope
import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.onStart
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

    override fun publishToRemote(value: ByteArray, eventUniqueId: String) {
        launch(Dispatchers.IO) {
            channel.queueDeclare(eventUniqueId, false, false, false, null)

            channel.basicPublish("", eventUniqueId, null, value)
        }
    }

    override fun <T : Any> listen(type: KClass<T>): Flow<T> {
        val eventUniqueId = enconder.typeUniqueId(type, listenTypes)

        return super.listen(type)
            .onStart {
                if(consumersTag.containsKey(eventUniqueId))
                    return@onStart

                // NoWait ?
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
            }
    }

    private fun insertConsumerTag(eventUniqueId: EventUniqueId, consumerTag: ConsumerTag) {
        consumersTag[eventUniqueId] = consumerTag
    }
}
