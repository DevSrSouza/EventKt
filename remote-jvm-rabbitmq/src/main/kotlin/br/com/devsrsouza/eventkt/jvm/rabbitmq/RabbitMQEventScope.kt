package br.com.devsrsouza.eventkt.jvm.rabbitmq

import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import br.com.devsrsouza.eventkt.remote.RemoteEventScope
import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class RabbitMQEventScope(
    override val enconder: RemoteEncoder<ByteArray>,
    val channel: Channel
) : RemoteEventScope<ByteArray>() {
    private val consumersTag: MutableList<String> = mutableListOf()

    override fun publishToRemote(value: ByteArray, eventUniqueId: String) {
        launch(Dispatchers.IO) {
            channel.queueDeclare(eventUniqueId, false, false, false, null)

            channel.basicPublish("", eventUniqueId, null, value)
        }
    }

    override fun <T : Any> listen(type: KClass<T>): Flow<T> {
        return super.listen(type)
            .onStart {
                val eventUniqueId = enconder.typeUniqueId(type, listenTypes)

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

                insertConsumerTag(consumerTag)
            }
    }

    private fun insertConsumerTag(consumerTag: String) {
        // TODO: check for concurrency problems
        consumersTag += consumerTag
    }
}
