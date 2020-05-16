object Libs {

    val kotlinX = KotlinX

    const val jedis = "redis.clients:jedis:3.2.0"

    object KotlinX {
        val serialization = Serialization

        object Serialization {
            private const val version = "0.20.0"
            private val prefix = "org.jetbrains.kotlinx:kotlinx-serialization"

            val runtimeJvm = "$prefix-runtime:$version"
            val runtimeNative = "$prefix-runtime-native:$version"
            val protoBufNative = "$prefix-protobuf-native:$version"
        }
    }
}