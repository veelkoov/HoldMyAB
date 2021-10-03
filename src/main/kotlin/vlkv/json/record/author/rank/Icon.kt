package vlkv.json.record.author.rank

import kotlinx.serialization.Serializable
import vlkv.json.record.author.rank.icon.Data

@Serializable
data class Icon(
    val base: String,
    val `data`: Data,
    val hiddenQueryString: List<String?>,
    val queryString: List<String?>,
    val seoPagination: String?
)
