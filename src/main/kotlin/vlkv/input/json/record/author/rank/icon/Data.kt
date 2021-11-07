package vlkv.input.json.record.author.rank.icon

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val fragment: String?,
    val host: String,
    val pass: String?,
    val path: String,
    val port: Int?,
    val query: String,
    val scheme: String,
    val user: String?
)
