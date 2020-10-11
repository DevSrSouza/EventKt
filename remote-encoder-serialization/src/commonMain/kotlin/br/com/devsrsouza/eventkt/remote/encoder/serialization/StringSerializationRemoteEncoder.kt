package br.com.devsrsouza.eventkt.remote.encoder.serialization

import br.com.devsrsouza.eventkt.remote.ListenerTypeSet
import br.com.devsrsouza.eventkt.remote.RemoteDecodeResult
import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.*

class StringSerializationRemoteEncoder(
    val stringFormat: StringFormat
) : SerializationRemoteEncoder<String> {

    @OptIn(InternalSerializationApi::class)
    override fun encode(
        event: Any,
        listenTypes: ListenerTypeSet
    ): String {
        val type = event::class

        val serializer = type.serializer() as KSerializer<Any>

        val message = StringEventMessage(
            serializer.descriptor.serialName,
            stringFormat.encodeToString(serializer, event)
        )

        return stringFormat.encodeToString(StringEventMessage.serializer(), message)
    }

    @OptIn(InternalSerializationApi::class)
    override fun decode(
        value: String,
        listenTypes: ListenerTypeSet
    ): RemoteDecodeResult {
        val message = stringFormat.decodeFromString(StringEventMessage.serializer(), value)

        try {
            val serializer = getSerializer(message.type, listenTypes)

            val event = stringFormat.decodeFromString(serializer, message.content)

            return RemoteDecodeResult.Success(event)
        } catch (e: RemoteTypeNotListenException) {
            return RemoteDecodeResult.EventTypeNotFound
        }
    }

}
