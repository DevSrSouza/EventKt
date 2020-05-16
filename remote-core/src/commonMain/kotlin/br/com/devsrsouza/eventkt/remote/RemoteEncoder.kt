package br.com.devsrsouza.eventkt.remote

import kotlin.reflect.KClass

interface RemoteEncoder<T> {
    fun encode(any: Any, listenTypes: Map<String, KClass<*>>): T

    fun decode(value: T, listenTypes: Map<String, KClass<*>>): Any
}