package vlkv.input.json.record.author.customFields.customField1.fields

import kotlinx.serialization.Serializable

@Serializable
data class Field1(
    val name: String,
    val value: String?
)