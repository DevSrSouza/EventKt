import kotlinx.serialization.Serializable

@Serializable
data class TestClass(
    val name: String,
    val uniqueId: Long
)