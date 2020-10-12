import br.com.devsrsouza.eventkt.jvm.paho.MqttEventScope
import br.com.devsrsouza.eventkt.remote.encoder.serialization.BinarySerializationRemoteEncoder
import kotlinx.coroutines.delay
import kotlinx.serialization.protobuf.ProtoBuf
import org.eclipse.paho.client.mqttv3.MqttAsyncClient

suspend fun main() {
    val uri = "tcp://localhost:1883"
    val clientId = "Publisher"

    val mqttClient = MqttAsyncClient(uri, clientId)
    mqttClient.connect()

    val scope = MqttEventScope(
        BinarySerializationRemoteEncoder(ProtoBuf),
        mqttClient
    )

    for (i in 1..100) {
        println("Publishing $i")
        scope.publish(TestClass("DevSrSouza $i", i % 2 == 0))
        delay(500)
    }
}