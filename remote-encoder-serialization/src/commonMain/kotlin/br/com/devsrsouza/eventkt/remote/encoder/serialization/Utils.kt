package br.com.devsrsouza.eventkt.remote.encoder.serialization

import br.com.devsrsouza.eventkt.remote.ListenerTypeSet
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializerOrNull

@OptIn(InternalSerializationApi::class)
internal fun getSerializer(
    serialName: String,
    listenTypes: ListenerTypeSet
): KSerializer<Any> = listenTypes.mapNotNull { it.serializerOrNull() }
        .find { serialName == it.descriptor.serialName } as KSerializer<Any>?
        ?: throw RemoteTypeNotListenException()