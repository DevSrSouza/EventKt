package br.com.devsrsouza.eventkt.scopes

class LocalEventScope : BaseEventScope() {

    override fun publish(any: Any) {
        publishLocal(any)
    }

}
