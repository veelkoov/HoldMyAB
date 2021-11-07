package vlkv.input.json.record.author

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val formattedName: String,
    val id: Int,
    val name: String
)
