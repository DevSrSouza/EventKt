package br.com.devsrsouza.eventkt.scopes

class LocalEventScope : BaseEventScope() {

    override fun publish(any: Any) {
        listenedClasses[any::class]?.values?.forEach { listenCallbacks ->
            for (callback in listenCallbacks) {
                callback(any)
            }
        }
    }

}
