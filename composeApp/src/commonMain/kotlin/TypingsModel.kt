import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Typing(
    val inputs: Map<String, ActionType> = emptyMap(),
    val outputs: Map<String, ActionType> = emptyMap(),
)

@Serializable
data class ActionType(
    val type: ActionTypeEnum,
    val name: String? = null,
    @SerialName("named-values")
    val namedValues: Map<String, Int> = emptyMap(),
    val separator: String = "",
    @SerialName("allowed-values")
    val allowedValues: List<String> = emptyList(),
    @SerialName("list-item")
    val listItem: ActionType? = null,
)

@Serializable
enum class ActionTypeEnum {
    @SerialName("string")
    String,

    @SerialName("boolean")
    Boolean,

    @SerialName("integer")
    Integer,

    @SerialName("float")
    Float,

    @SerialName("list")
    List,

    @SerialName("enum")
    Enum,
}
