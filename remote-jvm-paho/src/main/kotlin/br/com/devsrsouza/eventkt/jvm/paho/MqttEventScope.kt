package br.com.devsrsouza.eventkt.jvm.paho

import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import br.com.devsrsouza.eventkt.remote.RemoteEventScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class MqttEventScope(
    override val enconder: RemoteEncoder<ByteArray>,
    val mqttClient: IMqttAsyncClient,
) : RemoteEventScope<ByteArray>() {
    companion object {
        private const val OPS_FIRE_AND_FORGET = 0
    }

    private val subscriptionsSharedFlow: MutableMap<String, SharedFlow<Any>> = ConcurrentHashMap()

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

        val sharedFlow = subscriptionsSharedFlow[eventUniqueId]

        if(sharedFlow != null) return sharedFlow as Flow<T>

        val subscriptionFlow = MutableSharedFlow<T>(extraBufferCapacity = 1)

        subscriptionFlow.subscriptionCount
            .map { count -> count > 0 }
            .distinctUntilChanged()
            .onEach { isActive ->
                if(isActive) {
                    mqttClient.subscribe(eventUniqueId, OPS_FIRE_AND_FORGET)
                } else {
                    mqttClient.unsubscribe(eventUniqueId)
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

}