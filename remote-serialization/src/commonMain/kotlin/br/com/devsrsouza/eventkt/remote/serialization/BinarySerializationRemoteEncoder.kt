package br.com.devsrsouza.eventkt.remote.serialization

import br.com.devsrsouza.eventkt.remote.ListenerTypeSet
import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

class BinarySerializationRemoteEncoder(
    val binaryFormat: BinaryFormat
) : RemoteEncoder<ByteArray> {

    @OptIn(ImplicitReflectionSerializer::class)
    override fun encode(
        any: Any,
        listenTypes: ListenerTypeSet
    ): ByteArray {
        val type = any::class

        val serializer = type.serializer() as KSerializer<Any>

        val message = BynaryEventMessage(
            serializer.descriptor.serialName,
            binaryFormat.dump(serializer, any)
        )

        return binaryFormat.dump(BynaryEventMessage.serializer(), message)
    }

    @OptIn(ImplicitReflectionSerializer::class)
    override fun decode(
        value: ByteArray,
        listenTypes: ListenerTypeSet
    ): Any {
        val message = binaryFormat.load(BynaryEventMessage.serializer(), value)

        val serializer = getSerializer(message.type, listenTypes)

        return binaryFormat.load(serializer, message.content)
    }

}
