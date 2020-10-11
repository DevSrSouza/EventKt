import br.com.devsrsouza.eventkt.jvm.rabbitmq.RabbitMQEventScope
import br.com.devsrsouza.eventkt.remote.encoder.serialization.BinarySerializationRemoteEncoder
import com.rabbitmq.client.*
import kotlinx.coroutines.delay
import kotlinx.serialization.protobuf.ProtoBuf

suspend fun main() {
    val factory = ConnectionFactory().apply {
        host = "localhost"
        exceptionHandler = object : ExceptionHandler {
            override fun handleUnexpectedConnectionDriverException(conn: Connection?, exception: Throwable?) {
                exception?.printStackTrace()
            }

            override fun handleReturnListenerException(channel: Channel?, exception: Throwable?) {
                exception?.printStackTrace()
            }

            override fun handleConfirmListenerException(channel: Channel?, exception: Throwable?) {
                exception?.printStackTrace()
            }

            override fun handleBlockedListenerException(connection: Connection?, exception: Throwable?) {
                exception?.printStackTrace()
            }

            override fun handleConsumerException(
                channel: Channel?,
                exception: Throwable?,
                consumer: Consumer?,
                consumerTag: String?,
                methodName: String?
            ) {
                exception?.printStackTrace()
            }

            override fun handleConnectionRecoveryException(conn: Connection?, exception: Throwable?) {
                exception?.printStackTrace()
            }

            override fun handleChannelRecoveryException(ch: Channel?, exception: Throwable?) {
                exception?.printStackTrace()
            }

            override fun handleTopologyRecoveryException(
                conn: Connection?,
                ch: Channel?,
                exception: TopologyRecoveryException?
            ) {
                exception?.printStackTrace()
            }

        }
    }

    val connection = factory.newConnection()
    val channel = connection.createChannel()

    val scope = RabbitMQEventScope(
        BinarySerializationRemoteEncoder(ProtoBuf),
        channel
    )

    for(i in 1..100) {
        scope.publish(TestClass("EventKt", i.toLong()))
        delay(500)
    }
}