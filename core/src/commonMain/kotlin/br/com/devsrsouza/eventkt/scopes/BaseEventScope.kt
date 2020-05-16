package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class BaseEventScope : EventScope {
    private val _listenTypes = HashMap<String, KClass<*>>()
    protected val listenTypes: Map<String, KClass<*>> = _listenTypes
    protected val publisherChannel = BroadcastChannel<Any>(1)

    override val coroutineContext: CoroutineContext = Dispatchers.Unconfined

    override fun <T : Any> listen(
        type: KClass<T>
    ): Flow<T> {
        insertListenType(type)

        return publisherChannel.asFlow()
            .filter { type.isInstance(it) }
            .map { it as T }
    }

    protected fun publishLocal(any: Any) {
        launch {
            publisherChannel.send(any)
        }
    }

    private fun insertListenType(type: KClass<*>) {
        _listenTypes.put(
            type.qualifiedName ?: error("EventKt does not support local or an anonymous class."),
            type
        )
    }
}