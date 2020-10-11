import br.com.devsrsouza.eventkt.jvm.rabbitmq.RabbitMQEventScope
import br.com.devsrsouza.eventkt.listen
import br.com.devsrsouza.eventkt.remote.encoder.serialization.BinarySerializationRemoteEncoder
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.protobuf.ProtoBuf

suspend fun main() {
    val factory = ConnectionFactory().apply {
        host = "localhost"
    }

    val connection = factory.newConnection()
    val channel = connection.createChannel()

    val worker1 = RabbitMQEventScope(
        BinarySerializationRemoteEncoder(ProtoBuf),
        channel
    )

    val worker2 = RabbitMQEventScope(
        BinarySerializationRemoteEncoder(ProtoBuf),
        channel
    )

    GlobalScope.launch {
        worker2.listen<TestClass>()
            .collect {
                println("Worker 2: $it")
            }
    }

    worker1.listen<TestClass>()
        .collect {
            println("Worker 1: $it")
        }
}