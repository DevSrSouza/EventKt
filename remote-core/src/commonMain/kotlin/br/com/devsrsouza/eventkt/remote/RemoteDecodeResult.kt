package br.com.devsrsouza.eventkt.remote

sealed class RemoteDecodeResult {
    data class Success(val event: Any) : RemoteDecodeResult()
    object EventTypeNotFound : RemoteDecodeResult()
}