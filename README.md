# EventKt
EventKt is a simple and lightweight kotlin multiplatform event bus library.

The library uses Kotlin Coroutines Flow for provide you the events.
Also the library provide support for remote event publishing and listening, you can check more at the [remote section](#Remote).

- [Principles](#Principles)
- [Getting Started](#Getting-Started)
- [Examples](#Examples)
- [Remote](#Remote)

## Principles
The EventKt is scoped based, this means that for you publish or listen for some event you need a [EventScope](/core/src/commonMain/kotlin/br/com/devsrsouza/eventkt/EventScope.kt).
The library provides a global scope [`GlobalEventScope`](/core/src/commonMain/kotlin/br/com/devsrsouza/eventkt/scopes/GlobalEventScope.kt).

## Getting Started

```groovy
repositories {
    maven {
        url = "http://nexus.devsrsouza.com.br/repository/maven-public/"
    }
}

dependencies {
    // multiplatform
    implementation("br.com.devsrsouza.eventkt:eventkt-core:0.1.0-SNAPSHOT")
}
```

**JVM target**:
`implementation("br.com.devsrsouza.eventkt:eventkt-core-jvm:0.1.0-SNAPSHOT")`


## Examples

```kotlin
import br.com.devsrsouza.eventkt.scopes.GlobalEventScope
import br.com.devsrsouza.eventkt.listen

data class OnSomethingHappen(val withValue: String)

GlobalEventScope.listen<OnSomethingHappen>()
    .onEach { (withValue) ->
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

SimpleGlobalEventScope.listen<OnSomethingHappen>(owner = this) { (withValue) ->
    println("Receive my event OnSomethingHappen was triggered with value: $withValue")
}

SimpleGlobalEventScope.publish(OnSomethingHappen("Hello World!"))
```

### Creating your own scope

```kotlin
import br.com.devsrsouza.eventkt.scopes.LocalEventScope
import br.com.devsrsouza.eventkt.listen

data class OnSomethingLocallyHappen(val withValue: String)

val yourScope = LocalEventScope()

yourScope.listen<OnSomethingLocallyHappen>()
    .onEach { (withValue) ->
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

In Android you could receive events directly in the Main Thread (UI Thread) using simples as well.
``SimpleGlobalEventScope.listen<OnSomethingHappen>(owner = this, coroutineScope = viewLifecycleOwner.lifecycleScope) {}``


### Unregistering callback listener
When using simple scope you should always **unregister** your owners on disable/destroy/stop a object that listen, like a Activity/Fragment/Service on Android

```kotlin
import br.com.devsrsouza.eventkt.scopes.asSimple

simpleEventScope.unregisterOwner(owner = this)
```

### withOwner extension

```kotlin
import br.com.devsrsouza.eventkt.scopes.asSimple

SimpleGlobalEventScope.withOwner(this) {
    listen<OnSomethingHappen> {
        println("Yeah!")
    }
}

// unregistering
SimpleGlobalEventScope.unregister(this)
```


### Combining EventScopes

```kotlin
val combinedScope: EventScope = LocalEventScope() + RedisEventScope()
```

## Remote

EventKt is design to be used with Remote event publisher, such as Redis Pub/Sub, WebsScket, Kafka, etc.

## Redis
The Redis implementation is JVM only and use [Jedis client](https://github.com/xetorthio/jedis).
By default, the implementation use [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) enconder for your events,
this means that you will need make your events `@Serializable`. You can change the default enconder in the `RedisEventScope` constructor.

```kotlin
dependencies {
    implementation("br.com.devsrsouza.eventkt:eventkt-remote-redis:0.1.0-SNAPSHOT")
}
```

#### Usage

```kotlin
val subscribe = Jedis("127.0.0.1").apply { connect() }
val publisher = Jedis("127.0.0.1").apply { connect() }

val redisScope = RedisEventScope(subscribe, publisher)
```

With this scope you can publish and listen to events from remote instances.

```kotlin
@Serializable
data class YourEventClass(val x: String)

redisScope.listen<YourEventClass>()
    .onEach { println(it) }
    .launchIn(GlobalScope)
```

