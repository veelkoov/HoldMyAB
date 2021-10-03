package vlkv.json.record.author.customFields

import kotlinx.serialization.Serializable
import vlkv.json.record.author.customFields.customField2.Fields

@Serializable
data class CustomField2(
    val fields: Fields,
    val name: String
)
