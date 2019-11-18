package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

data class ListenFunction(val function: suspend (Any) -> Unit, val context: CoroutineContext)

private typealias ListenedClass = KClass<Any>
private typealias Owner = Any
private typealias ListenFunctionMutableList = MutableList<ListenFunction>

abstract class BaseEventScope : EventScope {
    // TODO switch to AVL Tree or Red Black.
    private val listenedClasses: MutableMap<ListenedClass, MutableMap<Owner, ListenFunctionMutableList>> = mutableMapOf()
    private val mutex: Mutex = Mutex()

    override val coroutineContext: CoroutineContext = Dispatchers.Unconfined

    override fun <T : Any> listen(
        kClass: KClass<T>,
        owner: Any,
        context: CoroutineContext,
        onReceive: suspend (T) -> Unit
    ) {
        launchWithLock {
            listenedClasses.getOrPut(kClass as KClass<Any>) {
                mutableMapOf()
            }.getOrPut(owner) {
                mutableListOf()
            }.add(
                ListenFunction(onReceive as suspend (Any) -> Unit, context)
            )
        }
    }

    override fun unregister(owner: Any) {
        launchWithLock {
            listenedClasses.values.forEach { ownerMap ->
                ownerMap.remove(owner)
            }
        }
    }

    suspend fun getListenFunctions(kClass: KClass<Any>): List<ListenFunction> {
        return mutex.withLock {
            listenedClasses[kClass]?.flatMap { (_, listenFunctions) -> listenFunctions }
        } ?: emptyList()
    }

    private inline fun <T> launchWithLock(crossinline action: () -> T) = launch {
        mutex.withLock(action = action)
    }
}