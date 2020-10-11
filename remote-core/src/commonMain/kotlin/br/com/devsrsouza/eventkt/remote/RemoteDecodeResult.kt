package br.com.devsrsouza.eventkt.remote

sealed class RemoteDecodeResult {
    data class Success(val value: Any) : RemoteDecodeResult()
    object EventTypeNotFound : RemoteDecodeResult()
}