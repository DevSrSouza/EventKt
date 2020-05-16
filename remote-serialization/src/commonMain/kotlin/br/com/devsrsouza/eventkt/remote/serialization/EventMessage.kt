package br.com.devsrsouza.eventkt.remote.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class JsonEventMessage(
    val type: String,
    val content: JsonElement
)

@Serializable
data class BytesEventMessage(
    val type: String,
    val content: ByteArray
)