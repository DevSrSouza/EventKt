package br.com.devsrsouza.eventkt.remote.serialization

import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class JsonSerializationRemoteEncoder : RemoteEncoder<String> {

    @OptIn(ImplicitReflectionSerializer::class)
    override fun encode(
        any: Any,
        listenTypes: Map<String, KClass<*>>
    ): String {
        val type = any::class

        val serializer = type.serializer() as KSerializer<Any>
        val typeName = type.qualifiedName
            ?: error("Json Remote Enconder from EventKt does not support anonymous and local objects")


        val message = JsonEventMessage(
            typeName,
            Json.toJson(serializer, any)
        )

        return Json.stringify(JsonEventMessage.serializer(), message)
    }

    @OptIn(ImplicitReflectionSerializer::class)
    override fun decode(
        value: String,
        listenTypes: Map<String, KClass<*>>
    ): Any {
        val message = Json.parse(JsonEventMessage.serializer(), value)

        val type = listenTypes[message.type]
            ?: throw RemoteTypeNotListenException()

        val serializer = type.serializer() as KSerializer<Any>

        return Json.fromJson(serializer, message.content)
    }

}
