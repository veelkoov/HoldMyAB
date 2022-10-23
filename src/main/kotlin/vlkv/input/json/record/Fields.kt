package vlkv.input.json.record

import kotlinx.serialization.Serializable

@Serializable
data class Fields(
    var field_3: String,
    var field_4: String,
    var field_5: String,
    var field_6: String,
    val field_8: Int,
    val field_9: String,
    val field_16: String?,
    val field_17: String?,
    val field_28: String?,
) {
    fun isResolved(): Boolean {
        return when (field_17) {
            "Yes", "Resolved" -> true
            "No", "Not Resolved", null -> false // null means no "Yes"
            else -> error("field_17 has unexpected value: '$field_17'")
        }
    }

    fun isNsfw(): Boolean {
        return when (field_16) {
            "Yes" -> true
            "No", null -> false // null means no "Yes"
            else -> error("field_16 has unexpected value: '$field_16'")
        }
    }

    fun getWhere(): String {
        return field_6
    }

    fun setWhere(newWhere: String) {
        field_6 = newWhere
    }

    fun getWho() = field_5

    fun setWho(newWho: String) {
        field_5 = newWho
    }

    fun getTitle() = field_3

    fun setTitle(newTitle: String) {
        field_3 = newTitle
    }

    fun getDescription() = field_4

    fun setDescription(newDescription: String) {
        field_4 = newDescription
    }
}
