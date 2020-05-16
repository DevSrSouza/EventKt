package br.com.devsrsouza.eventkt.remote

import br.com.devsrsouza.eventkt.scopes.BaseEventScope

abstract class RemoteEventScope<T> : BaseEventScope() {
    abstract val enconder: RemoteEncoder<T>

    final override fun publish(any: Any) {
        publishToRemote(enconder.encode(any, listenTypes))
    }

    abstract fun publishToRemote(value: T)

    fun publishFromRemote(value: T) {
        publishLocal(enconder.decode(value, listenTypes))
    }
}