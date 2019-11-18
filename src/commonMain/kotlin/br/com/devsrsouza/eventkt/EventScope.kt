package br.com.devsrsouza.eventkt

import br.com.devsrsouza.eventkt.scopes.CombinedEventScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

interface EventScope : CoroutineScope {
    fun publish(any: Any)

    fun <T : Any> listen(
        kClass: KClass<T>,
        owner: Any,
        context: CoroutineContext = Dispatchers.Default,
        onReceive: suspend (T) -> Unit
    )

    fun unregister(owner: Any)
}

inline fun <reified T : Any> EventScope.listen(
    owner: Any,
    context: CoroutineContext = Dispatchers.Default,
    noinline onReceive: suspend (T) -> Unit
) {
    listen(T::class, owner, context, onReceive)
}

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
