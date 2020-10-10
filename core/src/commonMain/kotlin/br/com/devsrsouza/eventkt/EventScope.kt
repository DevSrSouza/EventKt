package br.com.devsrsouza.eventkt

import br.com.devsrsouza.eventkt.scopes.CombinedEventScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface EventScope : CoroutineScope {
    /**
     * Publish a new event [any].
     */
    fun publish(any: Any)

    /**
     * Receives events as Flow from the given [type].
     */
    fun <T : Any> listen(
        type: KClass<T>
    ): Flow<T>
}

/**
 * Receives events as Flow from the given [type].
 */
inline fun <reified T : Any> EventScope.listen(): Flow<T> = listen(T::class)

/**
 * Combining EventScope on one, flowing the plus order:
 * `first + second`
 *
 * Will publish as:
 * ```
 * first.publish()
 * second.publish()
 * ```
 */
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
