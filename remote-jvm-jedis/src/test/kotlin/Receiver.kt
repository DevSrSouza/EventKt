import br.com.devsrsouza.eventkt.redis.RedisEventScope
import br.com.devsrsouza.eventkt.listen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import redis.clients.jedis.Jedis


suspend fun main() {

    println("Startining receiver")
    val subscribe = Jedis("127.0.0.1")
    val publisher = Jedis("127.0.0.1")
    subscribe.connect()
    publisher.connect()
    println("Connected to redis")

    val scope = RedisEventScope(
        subscribe, publisher
    )

    scope.listen<TestClass>().onEach {
        println(it)
    }.launchIn(GlobalScope)

    delay(500000)
}