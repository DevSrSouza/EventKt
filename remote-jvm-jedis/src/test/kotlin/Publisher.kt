import br.com.devsrsouza.eventkt.redis.RedisEventScope
import kotlinx.coroutines.delay
import redis.clients.jedis.Jedis
import kotlin.random.Random

suspend fun main() {

    println("Startining publisher")
    val subscribe = Jedis("127.0.0.1")
    val publisher = Jedis("127.0.0.1")
    subscribe.connect()
    publisher.connect()
    println("Connected to redis")

    val scope = RedisEventScope(
        subscribe, publisher
    )

    for(i in 1..1000) {
        val obj = TestClass(
            "sample",
            "${Random.nextInt(1, 50)}"
        )

        println("Publishing: $obj")
        scope.publish(obj)

        delay(3000)
    }
}