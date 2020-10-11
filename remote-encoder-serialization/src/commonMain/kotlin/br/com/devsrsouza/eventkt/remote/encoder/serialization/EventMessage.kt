package br.com.devsrsouza.eventkt.remote.encoder.serialization

import kotlinx.serialization.Serializable

@Serializable
data class StringEventMessage(
    val type: String,
    val content: String
)

@Serializable
data class BinaryEventMessage(
    val type: String,
    val content: ByteArray
)