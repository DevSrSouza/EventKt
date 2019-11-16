package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope
import kotlin.reflect.KClass

private typealias ListenedClass = KClass<Any>
private typealias Owner = Any
private typealias ListenFunctions = MutableList<(Any) -> Unit>

abstract class BaseEventScope : EventScope {
    // TODO switch to AVL Tree or Red Black.
    // For better performance, Owner should has hashCode implemented.
    // ListenedClass will be more frequently used (publish) them Owner (unregister)
    // TODO concurrency with coroutines?
    protected val listenedClasses: MutableMap<ListenedClass, MutableMap<Owner, ListenFunctions>> = mutableMapOf()

    override fun <T : Any> listen(kClass: KClass<T>, owner: Any, onReceive: (T) -> Unit) {
        listenedClasses.getOrPut(kClass as KClass<Any>) {
            mutableMapOf()
        }.getOrPut(owner) {
            mutableListOf()
        }.add(onReceive as (Any) -> Unit)
    }

    override fun unregister(owner: Any) {
        listenedClasses.values.forEach { ownerMap ->
            ownerMap.remove(owner)
        }
    }
}