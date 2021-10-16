package vlkv.json

import kotlinx.serialization.Serializable
import vlkv.json.record.Author
import vlkv.json.record.Category
import vlkv.json.record.Fields

@Serializable
data class Record(
    val author: Author,
    val category: Category,
    val comments: Int,
    val date: String,
    val description: String,
    val featured: Boolean,
    val fields: Fields,
    val hidden: Boolean,
    val id: Int,
    val image: String?,
    val locked: Boolean,
    val pinned: Boolean,
    val prefix: String?,
    val rating: Int,
    val reviews: Int,
    val tags: List<String>,
    var title: String,
    val topic: String?,
    val url: String,
    val views: Int
) {
    fun isBeware(): Boolean {
        if (category.name.contains("beware", true)
            && !category.name.contains("caution", true)
        ) {
            return true
        }

        if (category.name.contains("caution", true)) {
            return false
        }

        error("Failed to decide if a beware or a caution: $title")
    }
}
