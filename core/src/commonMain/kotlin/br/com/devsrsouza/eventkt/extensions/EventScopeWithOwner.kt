package br.com.devsrsouza.eventkt.extensions

import br.com.devsrsouza.eventkt.EventScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class EventScopeWithOwner(val eventScope: EventScope, val owner: Any)

inline fun EventScope.withOwner(owner: Any, block: EventScopeWithOwner.() -> Unit) {
    EventScopeWithOwner(this, owner).block()
}

fun <T : Any> EventScopeWithOwner.listen(
    kClass: KClass<T>,
    context: CoroutineContext = Dispatchers.Default,
    onReceive: suspend (T) -> Unit
) {
    eventScope.listen(kClass, owner, context, onReceive)
}

inline fun <reified T : Any> EventScopeWithOwner.listen(
    context: CoroutineContext = Dispatchers.Default,
    noinline onReceive: suspend (T) -> Unit
) {
    listen(T::class, context, onReceive)
}