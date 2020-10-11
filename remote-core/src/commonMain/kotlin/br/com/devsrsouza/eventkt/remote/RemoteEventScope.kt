package br.com.devsrsouza.eventkt.remote

import br.com.devsrsouza.eventkt.scopes.BaseEventScope
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

typealias ListenerTypeSet = Set<KClass<*>>

abstract class RemoteEventScope<T> : BaseEventScope() {
    private val _listenTypes = mutableSetOf<KClass<*>>()
    protected val listenTypes: ListenerTypeSet
        get() = HashSet(_listenTypes)

    abstract val enconder: RemoteEncoder<T>

    final override fun publish(any: Any) {
        publishToRemote(enconder.encode(any, listenTypes))
    }

    override fun <T : Any> listen(type: KClass<T>): Flow<T> {
        insertListenType(type)

        return super.listen(type)
    }

    abstract fun publishToRemote(value: T)

    fun publishFromRemote(value: T) {
        val result = enconder.decode(value, listenTypes)

        when(result) {
            is RemoteDecodeResult.Success -> {
                publishLocal(result.value)
            }
            RemoteDecodeResult.EventTypeNotFound -> {
                // ignore
            }
        }

    }

    private fun insertListenType(type: KClass<*>) {
        // TODO: check for concurrency problems
        _listenTypes.add(type)
    }
}