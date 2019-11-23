package br.com.devsrsouza.eventkt.scopes

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

class LocalEventScope : BaseEventScope() {

    override fun publish(any: Any) {
        launch {
            getListenFunctions(any::class as KClass<Any>).forEach { listenFunction ->
                withContext(listenFunction.context) {
                    listenFunction.function(any)
                }
            }
        }
    }

}
