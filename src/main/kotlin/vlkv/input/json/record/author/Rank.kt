package vlkv.input.json.record.author

import kotlinx.serialization.Serializable
import vlkv.input.json.record.author.rank.Icon

@Serializable
data class Rank(
    val icon: Icon,
    val id: Int,
    val name: String,
    val points: Int
)
