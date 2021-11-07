package vlkv.input.json.record

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val `class`: String,
    val id: Int,
    val name: String,
    val parentId: Int,
    val permissions: Permissions,
    val url: String
)
