package br.com.devsrsouza.eventkt.remote.encoder.serialization

import br.com.devsrsouza.eventkt.remote.ListenerTypeSet
import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.*

class StringSerializationRemoteEncoder(
    val stringFormat: StringFormat
) : RemoteEncoder<String> {

    @OptIn(InternalSerializationApi::class)
    override fun encode(
        any: Any,
        listenTypes: ListenerTypeSet
    ): String {
        val type = any::class

        val serializer = type.serializer() as KSerializer<Any>

        val message = StringEventMessage(
            serializer.descriptor.serialName,
            stringFormat.encodeToString(serializer, any)
        )

        return stringFormat.encodeToString(StringEventMessage.serializer(), message)
    }

    @OptIn(InternalSerializationApi::class)
    override fun decode(
        value: String,
        listenTypes: ListenerTypeSet
    ): Any {
        val message = stringFormat.decodeFromString(StringEventMessage.serializer(), value)

        val serializer = getSerializer(message.type, listenTypes)

        return stringFormat.decodeFromString(serializer, message.content)
    }

}
