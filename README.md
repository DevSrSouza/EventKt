# EventKt
EventKt is a simple and lightweight kotlin multiplatform event bus library

## Principles
The EventKt is scoped based, this means that for you publish or listen for some event you need a [EventScope](/core/src/commonMain/kotlin/br/com/devsrsouza/eventkt/EventScope.kt).
The library provides a global scope [`GlobalEventScope`](/core/src/commonMain/kotlin/br/com/devsrsouza/eventkt/scopes/GlobalEventScope.kt)

## Samples

```kotlin
import br.com.devsrsouza.eventkt.scopes.GlobalEventScope
import br.com.devsrsouza.eventkt.listen

data class OnSomethingHappen(val withValue: String)

GlobalEventScope.listen<OnSomethingHappen>(owner = this) { (withValue) ->
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

yourScope.listen<OnSomethingLocallyHappen>(owner = this) { (withValue) ->
    println("Receive my event OnSomethingLocallyHappen was triggered with value: $withValue")
}

yourScope.publish(OnSomethingHappen("Hello Local World!"))
```

### Listen in your coroutine context

```kotlin
import br.com.devsrsouza.eventkt.scopes.GlobalEventScope
import br.com.devsrsouza.eventkt.listen

val singleThreadContext = newSingleThreadContext("EventReceiverThread")

data class OnSomethingHappen()

GlobalEventScope.listen<OnSomethingHappen>(owner = this, context = singleThreadContext) { (withValue) ->
    println("Receive my event in thread: ${Thread.currentThread().name}")
}

GlobalEventScope.publish(OnSomethingHappen())
```

In Android you could receive events directly in the Main Thread (UI Thread)
``GlobalEventScope.listen<OnSomethingHappen>(owner = this, context = Dispatchers.Main)``


### Unregistering listen
You should always **unregister** your owners on disable/destroy/stop a object that listen, like a Activity/Fragment/Service on Android

```kotlin
import br.com.devsrsouza.eventkt.scopes.GlobalEventScope

GlobalEventScope.unregisterOwner(owner = this)
```

### withOwner extension

```kotlin
GlobalEventScope.withOwner(this) {
    listen<OnSomethingHappen> {
        println("Yeah!")
    }
}

// unregistering
GlobalEventScope.unregister(this)
```


### Combining EventScopes

```kotlin
val combinedScope: EventScope = LocalEventScope() + RedisEventScope()
```

