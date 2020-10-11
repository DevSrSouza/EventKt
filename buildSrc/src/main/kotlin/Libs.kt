object Libs {

    val kotlinVersion = "1.4.10"

    val kotlinX = KotlinX
    val mockK = MockK

    const val jedis = "redis.clients:jedis:3.2.0"
    const val rabbitMq = "com.rabbitmq:amqp-client:5.9.0"

    object KotlinX {
        val serialization = Serialization
        val coroutines = Coroutines

        object Serialization {
            private const val version = "1.0.0"
            private const val prefix = "org.jetbrains.kotlinx:kotlinx-serialization"

            val core = "$prefix-core:$version"
            val json = "$prefix-json:$version"
            val protoBuf = "$prefix-protobuf:$version"
        }

        object Coroutines {
            private const val version = "1.3.9"
            private const val prefix = "org.jetbrains.kotlinx:kotlinx-coroutines"

            val core = "$prefix-core:$version"
            val test = "$prefix-test:$version"
        }
    }

    object MockK {
        private const val version = "1.10.2"
        private const val prefix = "io.mockk:mockk"

        val jvm = "$prefix:$version"
        val common = "$prefix-common:$version"
    }
}