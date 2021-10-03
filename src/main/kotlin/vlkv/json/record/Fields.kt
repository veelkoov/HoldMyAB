package vlkv.json.record

import kotlinx.serialization.Serializable

private const val FLD_VAL_YES = "Yes"
private const val FLD_VAL_NO = "No"

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
        return if (FLD_VAL_YES == field_17) {
            true
        } else if (FLD_VAL_NO == field_17) {
            false
        } else if (null == field_17) {
            false // null means no "Yes"
        } else {
            error("field_17 has unexpected value")
        }
    }

    fun getWhere(): String {
        return field_6
    }
}
