package vlkv.input.json.record.author.customFields

import kotlinx.serialization.Serializable
import vlkv.input.json.record.author.customFields.customField1.Fields

@Serializable
data class CustomField1(
    val fields: Fields,
    val name: String
)