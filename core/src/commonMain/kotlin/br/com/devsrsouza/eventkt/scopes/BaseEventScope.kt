package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class BaseEventScope : EventScope {
    protected val publisherChannel = ConflatedBroadcastChannel<Any>()

    override val coroutineContext: CoroutineContext = Dispatchers.Unconfined

    override fun <T : Any> listen(
        type: KClass<T>
    ): Flow<T> = publisherChannel.asFlow()
        .filter { type.isInstance(it) }
        .map { it as T }

    override fun publishLocal(any: Any) {
        launch {
            publisherChannel.send(any)
        }
    }
}