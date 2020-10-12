import br.com.devsrsouza.eventkt.jvm.paho.MqttEventScope
import br.com.devsrsouza.eventkt.listen
import br.com.devsrsouza.eventkt.remote.encoder.serialization.BinarySerializationRemoteEncoder
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.protobuf.ProtoBuf
import org.eclipse.paho.client.mqttv3.MqttAsyncClient

suspend fun main() {
    val uri = "tcp://localhost:1883"
    val clientId = "Subscriber"

    val mqttClient = MqttAsyncClient(uri, clientId)
    mqttClient.connect()

    val scope = MqttEventScope(
        BinarySerializationRemoteEncoder(ProtoBuf),
        mqttClient
    )

    scope.listen<TestClass>()
        .collect {
            println("Receive: $it")
        }
}