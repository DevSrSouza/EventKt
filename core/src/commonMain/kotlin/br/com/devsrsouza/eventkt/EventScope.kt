package br.com.devsrsouza.eventkt

import br.com.devsrsouza.eventkt.scopes.CombinedEventScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface EventScope : CoroutineScope {
    /**
     * Publish the given [any] depending on the [EventScope] implementation.
     *
     * For [LocalEventScope] this function works exactly as [publishLocal].
     * For a Redis implementation, this function could send a event using Redis Pub/Sub.
     */
    fun publish(any: Any)

    /**
     * Publish the given [any] to the current app instance.
     */
    fun publishLocal(any: Any)

    /**
     * Receives a Flow to listen to events from the given [type].
     */
    fun <T : Any> listen(
        type: KClass<T>
    ): Flow<T>
}

inline fun <reified T : Any> EventScope.listen(): Flow<T> = listen(T::class)

operator fun EventScope.plus(eventScope: EventScope): EventScope {
    return when {
        this is CombinedEventScope -> clone().apply {
            addScope(eventScope)
        }
        eventScope is CombinedEventScope -> eventScope.clone().apply {
            addScopeToTop(this@plus)
        }
        else -> CombinedEventScope().apply {
            addScope(this)
            addScope(eventScope)
        }
    }
}
