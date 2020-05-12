package br.com.devsrsouza.eventkt.extensions

import br.com.devsrsouza.eventkt.scopes.SimpleEventScope
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

class EventScopeWithOwner(val eventScope: SimpleEventScope, val owner: Any)

inline fun SimpleEventScope.withOwner(owner: Any, block: EventScopeWithOwner.() -> Unit) {
    EventScopeWithOwner(this, owner).block()
}

fun <T : Any> EventScopeWithOwner.listen(
    kClass: KClass<T>,
    coroutineScope: CoroutineScope,
    onReceive: suspend (T) -> Unit
) {
    eventScope.listen(kClass, owner, coroutineScope, onReceive)
}

inline fun <reified T : Any> EventScopeWithOwner.listen(
    coroutineScope: CoroutineScope,
    noinline onReceive: suspend (T) -> Unit
) {
    listen(T::class, coroutineScope, onReceive)
}