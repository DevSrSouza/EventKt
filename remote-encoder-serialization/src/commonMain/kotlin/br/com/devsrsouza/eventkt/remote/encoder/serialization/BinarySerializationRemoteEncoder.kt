package br.com.devsrsouza.eventkt.remote.encoder.serialization

import br.com.devsrsouza.eventkt.remote.ListenerTypeSet
import br.com.devsrsouza.eventkt.remote.RemoteDecodeResult
import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

class BinarySerializationRemoteEncoder(
    val binaryFormat: BinaryFormat
) : SerializationRemoteEncoder<ByteArray> {

    @OptIn(InternalSerializationApi::class)
    override fun encode(
        event: Any,
        listenTypes: ListenerTypeSet
    ): ByteArray {
        val type = event::class

        val serializer = type.serializer() as KSerializer<Any>

        val message = BinaryEventMessage(
            serializer.descriptor.serialName,
            binaryFormat.encodeToByteArray(serializer, event)
        )

        return binaryFormat.encodeToByteArray(BinaryEventMessage.serializer(), message)
    }

    @OptIn(InternalSerializationApi::class)
    override fun decode(
        value: ByteArray,
        listenTypes: ListenerTypeSet
    ): RemoteDecodeResult {
        val message = binaryFormat.decodeFromByteArray(BinaryEventMessage.serializer(), value)

        try {
            val serializer = getSerializer(message.type, listenTypes)

            val event = binaryFormat.decodeFromByteArray(serializer, message.content)

            return RemoteDecodeResult.Success(event)
        } catch (e: RemoteTypeNotListenException) {
            return RemoteDecodeResult.EventTypeNotFound
        }
    }

}
