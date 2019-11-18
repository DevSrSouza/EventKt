package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass

class CombinedEventScope(
    private val scopes: MutableList<EventScope> = mutableListOf()
) : EventScope {
    override val coroutineContext: CoroutineContext = EmptyCoroutineContext

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

    override fun <T : Any> listen(
        kClass: KClass<T>,
        owner: Any,
        context: CoroutineContext,
        onReceive: suspend (T) -> Unit
    ) {
        for (scope in scopes) {
            scope.listen(kClass, owner, context, onReceive)
        }
    }

    override fun unregister(owner: Any) {
        for (scope in scopes) {
            scope.unregister(owner)
        }
    }

    fun clone() = CombinedEventScope(scopes.toMutableList())
}