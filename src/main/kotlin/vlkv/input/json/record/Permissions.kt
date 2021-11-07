package vlkv.input.json.record

import kotlinx.serialization.Serializable

@Serializable
data class Permissions(
    val perm_2: String,
    val perm_3: String,
    val perm_4: String,
    val perm_5: String,
    val perm_6: String,
    val perm_7: String,
    val perm_id: Int,
    val perm_view: String
)
