package vlkv.input.json.record

import kotlinx.serialization.Serializable

@Serializable
data class Reaction(
    val title: String,
    val id: Int,
    val value: Int,
    val icon: String,
)
