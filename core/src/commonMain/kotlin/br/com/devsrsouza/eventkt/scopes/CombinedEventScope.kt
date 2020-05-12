package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
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

    override fun publishLocal(any: Any) {
        for (scope in scopes) {
            scope.publishLocal(any)
        }
    }

    override fun <T : Any> listen(type: KClass<T>): Flow<T> {
        return merge(*scopes.map { it.listen(type) }.toTypedArray())
    }

    fun clone() = CombinedEventScope(scopes.toMutableList())
}