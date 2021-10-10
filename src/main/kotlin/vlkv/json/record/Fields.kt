package vlkv.json.record

import kotlinx.serialization.Serializable

@Serializable
data class Fields(
    val field_3: String,
    val field_4: String,
    val field_5: String,
    val field_6: String,
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

    fun getWhere(): String {
        return field_6
    }

    fun getWho(): String {
        return field_5
    }

    fun getTitle(): String {
        return field_3
    }

    fun getDescription(): String {
        return field_4
    }
}
