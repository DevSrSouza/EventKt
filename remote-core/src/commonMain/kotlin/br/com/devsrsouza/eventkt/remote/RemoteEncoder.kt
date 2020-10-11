package br.com.devsrsouza.eventkt.remote

import kotlin.reflect.KClass

interface RemoteEncoder<T> {
    /**
     * Encodes the given [event] instance in [T].
     */
    fun encode(event: Any, listenTypes: ListenerTypeSet): T

    /**
     * Decodes [T] into the event class instance.
     */
    fun decode(value: T, listenTypes: ListenerTypeSet): RemoteDecodeResult

    /**
     * A unique id for the given [eventType]. This could be the Fully qualified name
     * of the [eventType] or any type of serial id.
     */
    fun typeUniqueId(eventType: KClass<out Any>, listenTypes: ListenerTypeSet): String
}