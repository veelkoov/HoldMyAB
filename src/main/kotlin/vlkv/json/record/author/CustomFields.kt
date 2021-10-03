package vlkv.json.record.author

import kotlinx.serialization.Serializable
import vlkv.json.record.author.customFields.CustomField1
import vlkv.json.record.author.customFields.CustomField2

@Serializable
data class CustomFields(
    val `1`: CustomField1,
    val `2`: CustomField2
)
