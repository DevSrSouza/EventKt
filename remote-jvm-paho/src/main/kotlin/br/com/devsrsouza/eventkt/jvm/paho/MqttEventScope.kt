package br.com.devsrsouza.eventkt.jvm.paho

import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import br.com.devsrsouza.eventkt.remote.RemoteEventScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.*
import kotlin.reflect.KClass

class MqttEventScope(
    override val enconder: RemoteEncoder<ByteArray>,
    val mqttClient: IMqttAsyncClient,
) : RemoteEventScope<ByteArray>() {
    companion object {
        private const val OPS_FIRE_AND_FORGET = 0
    }

    private val subscribedTopics: MutableList<String> = mutableListOf()

    init {
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                // TODO?
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                publishFromRemote(message.payload)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // nothing
            }
        })
    }

    override fun publishToRemote(value: ByteArray, eventUniqueId: String) {
        launch(Dispatchers.IO) {
            mqttClient.publish(eventUniqueId, value, OPS_FIRE_AND_FORGET, false)
        }
    }

    override fun <T : Any> listen(type: KClass<T>): Flow<T> {
        val eventUniqueId = enconder.typeUniqueId(type, listenTypes)

        return super.listen(type)
            .onStart {
                if(eventUniqueId in subscribedTopics)
                    return@onStart

                subscribedTopics += eventUniqueId

                mqttClient.subscribe(eventUniqueId, OPS_FIRE_AND_FORGET)
            }
    }

}