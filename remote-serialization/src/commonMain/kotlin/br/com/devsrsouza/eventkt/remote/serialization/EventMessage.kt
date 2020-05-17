package br.com.devsrsouza.eventkt.remote.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class StringEventMessage(
    val type: String,
    val content: String
)

@Serializable
data class BynaryEventMessage(
    val type: String,
    val content: ByteArray
)