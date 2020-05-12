package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.reflect.KClass

fun EventScope.asSimple(): SimpleEventScope = SimpleEventScope(this)

inline fun <reified T : Any> SimpleEventScope.listen(
    owner: Any,
    coroutineScope: CoroutineScope,
    noinline onReceive: suspend (T) -> Unit
) {
    listen(T::class, owner, coroutineScope, onReceive)
}

class SimpleEventScope(
    val delegate: EventScope
) : EventScope by delegate {
    private val listenedClasses: HashMap<ListenedClass, MutableMap<Owner, ListenFunctionMutableList>> = hashMapOf()
    private val mutex: Mutex = Mutex()

    fun <T : Any> listen(
        type: KClass<T>,
        owner: Any,
        coroutineScope: CoroutineScope,
        onReceive: suspend (T) -> Unit
    ) {
        val job = listen(type)
            .onEach(onReceive)
            .launchIn(coroutineScope)

        launchWithLock {
            listenedClasses.getOrPut(type as KClass<Any>) {
                mutableMapOf()
            }.getOrPut(owner) {
                mutableListOf()
            }.add(
                ListenFunction(onReceive as suspend (Any) -> Unit, job)
            )
        }
    }

    fun unregister(owner: Any) {
        launchWithLock {
            listenedClasses.values.forEach { ownerMap ->
                ownerMap.remove(owner)
                    ?.forEach { it.flowJob.cancel() } // cancelling flows
            }
        }
    }

    private inline fun <T> launchWithLock(crossinline action: () -> T) = launch {
        mutex.withLock(action = action)
    }
}

private data class ListenFunction(val function: suspend (Any) -> Unit, val flowJob: Job)

private typealias ListenedClass = KClass<Any>
private typealias Owner = Any
private typealias ListenFunctionMutableList = MutableList<ListenFunction>