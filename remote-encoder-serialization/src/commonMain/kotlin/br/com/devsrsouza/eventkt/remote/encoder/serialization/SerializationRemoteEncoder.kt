package br.com.devsrsouza.eventkt.remote.encoder.serialization

import br.com.devsrsouza.eventkt.remote.ListenerTypeSet
import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

interface SerializationRemoteEncoder<T> : RemoteEncoder<T> {

    @OptIn(InternalSerializationApi::class)
    override fun typeUniqueId(eventType: KClass<out Any>, listenTypes: ListenerTypeSet): String {
        val serializer = eventType.serializer()

        return serializer.descriptor.serialName
    }
}