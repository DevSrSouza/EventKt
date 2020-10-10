package br.com.devsrsouza.eventkt.remote.encoder.serialization

import kotlinx.serialization.Serializable

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