# EventKt
EventKt is a simple and lightweight kotlin multiplatform event bus library

- [Principles](Principles)
- [Get starter]()
- [Examples](Examples)

## Principles
The EventKt is scoped based, this means that for you publish or listen for some event you need a [EventScope](/core/src/commonMain/kotlin/br/com/devsrsouza/eventkt/EventScope.kt).
The library provides a global scope [`GlobalEventScope`](/core/src/commonMain/kotlin/br/com/devsrsouza/eventkt/scopes/GlobalEventScope.kt)

## Getting Starter

## Examples

```kotlin
import br.com.devsrsouza.eventkt.scopes.GlobalEventScope
import br.com.devsrsouza.eventkt.listen

data class OnSomethingHappen(val withValue: String)

GlobalEventScope.listen<OnSomethingHappen>().onEach { (withValue) ->
    println("Receive my event OnSomethingHappen was triggered with value: $withValue")
}.launchIn(myCoroutineScope)

GlobalEventScope.publish(OnSomethingHappen("Hello World!"))
```

### Using with callback

```kotlin
import br.com.devsrsouza.eventkt.scopes.GlobalEventScope
import br.com.devsrsouza.eventkt.scopes.asSimple
import br.com.devsrsouza.eventkt.listen

data class OnSomethingHappen(val withValue: String)

GlobalEventScope.asSimple().listen<OnSomethingHappen>(owner = this) { (withValue) ->
    println("Receive my event OnSomethingHappen was triggered with value: $withValue")
}

GlobalEventScope.publish(OnSomethingHappen("Hello World!"))
```

### Creating your own scope

```kotlin
import br.com.devsrsouza.eventkt.scopes.LocalEventScope
import br.com.devsrsouza.eventkt.listen

data class OnSomethingLocallyHappen(val withValue: String)

val yourScope = LocalEventScope()

yourScope.listen<OnSomethingLocallyHappen>().onEach { (withValue) ->
    println("Receive my event OnSomethingLocallyHappen was triggered with value: $withValue")
}.launchIn(myCoroutineScope)

yourScope.publish(OnSomethingHappen("Hello Local World!"))
```

### Listen in your coroutine scope

```kotlin
import br.com.devsrsouza.eventkt.scopes.GlobalEventScope
import br.com.devsrsouza.eventkt.scopes.asSimple
import br.com.devsrsouza.eventkt.listen

val singleThreadContext = newSingleThreadContext("EventReceiverThread")
val myCoroutineScope = CoroutineScope(singleThreadContext)

data class OnSomethingHappen()

val simpleEventScope = GlobalEventScope.asSimple()

simpleEventScope.listen<OnSomethingHappen>(owner = this, coroutineScope = myCoroutineScope) { (withValue) ->
    println("Receive my event in thread: ${Thread.currentThread().name}")
}

GlobalEventScope.publish(OnSomethingHappen())
```

In Android you could receive events directly in the Main Thread (UI Thread)
``GlobalEventScope.listen<OnSomethingHappen>(owner = this, context = Dispatchers.Main)``


### Unregistering callback listener
You should always **unregister** your owners on disable/destroy/stop a object that listen, like a Activity/Fragment/Service on Android

```kotlin
import br.com.devsrsouza.eventkt.scopes.asSimple

simpleEventScope.unregisterOwner(owner = this)
```

### withOwner extension

```kotlin
import br.com.devsrsouza.eventkt.scopes.asSimple

val simpleEventScope = GlobalEventScope.asSimple()

simpleEventScope.withOwner(this) {
    listen<OnSomethingHappen> {
        println("Yeah!")
    }
}

// unregistering
simpleEventScope.unregister(this)
```


### Combining EventScopes

```kotlin
val combinedScope: EventScope = LocalEventScope() + RedisEventScope()
```

