package br.com.devsrsouza.eventkt

import br.com.devsrsouza.eventkt.scopes.MultipleEventScope
import kotlin.reflect.KClass

interface EventScope {
    fun publish(any: Any)

    fun <T : Any> listen(kClass: KClass<T>, owner: Any, onReceive: (T) -> Unit)

    fun unregister(owner: Any)
}

inline fun <reified T : Any> EventScope.listen(owner: Any, noinline onReceive: (T) -> Unit) {
    listen(T::class, owner, onReceive)
}

operator fun EventScope.plus(eventScope: EventScope): EventScope {
    return when {
        this is MultipleEventScope -> clone().apply {
            addScope(eventScope)
        }
        eventScope is MultipleEventScope -> eventScope.clone().apply {
            addScopeToTop(this@plus)
        }
        else -> MultipleEventScope().apply {
            addScope(this)
            addScope(eventScope)
        }
    }
}
