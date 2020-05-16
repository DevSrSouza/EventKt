package br.com.devsrsouza.eventkt.remote.serialization

import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class ProtobufSerializationRemoteEncoder : RemoteEncoder<ByteArray> {

    @OptIn(ImplicitReflectionSerializer::class)
    override fun encode(
        any: Any,
        listenTypes: Map<String, KClass<*>>
    ): ByteArray {
        val type = any::class

        val serializer = type.serializer() as KSerializer<Any>
        val typeName = type.qualifiedName
            ?: error("ProtoBuf Remote Enconder from EventKt does not support anonymous and local objects")

        val message = BytesEventMessage(
            typeName,
            ProtoBuf.dump(serializer, any)
        )

        return ProtoBuf.dump(BytesEventMessage.serializer(), message)
    }

    @OptIn(ImplicitReflectionSerializer::class)
    override fun decode(
        value: ByteArray,
        listenTypes: Map<String, KClass<*>>
    ): Any {
        val message = ProtoBuf.load(BytesEventMessage.serializer(), value)

        val type = listenTypes[message.type]
            ?: throw RemoteTypeNotListenException()

        val serializer = type.serializer() as KSerializer<Any>

        return ProtoBuf.load(serializer, message.content)
    }

}
