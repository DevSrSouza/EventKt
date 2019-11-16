package br.com.devsrsouza.eventkt.extensions

import br.com.devsrsouza.eventkt.EventScope
import kotlin.reflect.KClass

class EventScopeWithOwner(val eventScope: EventScope, val owner: Any)

inline fun EventScope.withOwner(owner: Any, block: EventScopeWithOwner.() -> Unit) {
    EventScopeWithOwner(this, owner).block()
}

fun <T : Any> EventScopeWithOwner.listen(kClass: KClass<T>, onReceive: (T) -> Unit) {
    eventScope.listen(kClass, owner, onReceive)
}

inline fun <reified T : Any> EventScopeWithOwner.listen(noinline onReceive: (T) -> Unit) {
    listen(T::class, onReceive)
}