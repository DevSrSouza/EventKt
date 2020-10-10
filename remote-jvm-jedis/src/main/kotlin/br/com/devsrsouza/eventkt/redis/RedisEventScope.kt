package br.com.devsrsouza.eventkt.redis

import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import br.com.devsrsouza.eventkt.remote.RemoteEventScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class RedisEventScope(
    override val enconder: RemoteEncoder<String>,
    val subscribeJedis: Jedis,
    val publisherJedis: Jedis,
    val channelName: String = "br.com.devsrsouza.eventkt"
) : RemoteEventScope<String>() {

    private val pubSub = object : JedisPubSub() {
        override fun onMessage(channel: String, message: String) {
            if(channelName == channel) {
                try {
                  publishFromRemote(message)
                }catch (e: Throwable) { /* Ignore */}
            }
        }
    }

    init {
        Thread {
            subscribeJedis.subscribe(pubSub, channelName)
        }.start()
    }

    override fun publishToRemote(value: String) {
        launch(Dispatchers.IO) {
            publisherJedis.publish(channelName, value)
        }
    }
}