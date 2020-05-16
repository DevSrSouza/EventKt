import kotlinx.serialization.Serializable

@Serializable
data class TestClass(
    val txt: String,
    val secondValue: String
)