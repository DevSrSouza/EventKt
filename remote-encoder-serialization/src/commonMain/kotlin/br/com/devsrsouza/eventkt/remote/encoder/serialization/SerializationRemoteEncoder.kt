package br.com.devsrsouza.eventkt.remote.encoder.serialization

import br.com.devsrsouza.eventkt.remote.ListenerTypeSet
import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

interface SerializationRemoteEncoder<T> : RemoteEncoder<T> {

    @OptIn(InternalSerializationApi::class)
    override fun typeUniqueId(event: Any, listenTypes: ListenerTypeSet): String {
        val type = event::class

        val serializer = type.serializer() as KSerializer<Any>

        return serializer.descriptor.serialName
    }
}