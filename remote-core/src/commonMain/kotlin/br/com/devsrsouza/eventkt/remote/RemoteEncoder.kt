package br.com.devsrsouza.eventkt.remote

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
     * A unique id for the given [event] type. This could be the Fully qualified name
     * of the class from [event] or any type of serial id for the [event] class.
     */
    fun typeUniqueId(event: Any, listenTypes: ListenerTypeSet): String
}