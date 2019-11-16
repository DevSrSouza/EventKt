package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope
import kotlin.reflect.KClass

class MultipleEventScope(
    private val scopes: MutableList<EventScope> = mutableListOf()
) : EventScope {

    fun addScope(eventScope: EventScope) {
        scopes.add(eventScope)
    }

    fun addScopeToTop(eventScope: EventScope) {
        scopes.add(0, eventScope)
    }

    override fun publish(any: Any) {
        for (scope in scopes) {
            scope.publish(any)
        }
    }

    override fun <T : Any> listen(kClass: KClass<T>, owner: Any, onReceive: (T) -> Unit) {
        for (scope in scopes) {
            scope.listen(kClass, owner, onReceive)
        }
    }

    override fun unregister(owner: Any) {
        for (scope in scopes) {
            scope.unregister(owner)
        }
    }

    fun clone() = MultipleEventScope(scopes.toMutableList())
}