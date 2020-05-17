package br.com.devsrsouza.eventkt.remote

interface RemoteEncoder<T> {
    fun encode(any: Any, listenTypes: ListenerTypeSet): T

    fun decode(value: T, listenTypes: ListenerTypeSet): Any
}