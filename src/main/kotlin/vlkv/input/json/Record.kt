package vlkv.input.json

import kotlinx.serialization.Serializable
import vlkv.input.json.record.Author
import vlkv.input.json.record.Category
import vlkv.input.json.record.Fields
import vlkv.input.json.record.Reaction

@Serializable
data class Record(
    val author: Author,
    val category: Category,
    val comments: Int,
    val date: String,
    var description: String,
    val featured: Boolean,
    val fields: Fields,
    val hidden: Boolean,
    val id: Int,
    val image: String?,
    val locked: Boolean,
    val pinned: Boolean,
    val prefix: String?,
    val rating: Int,
    val reactions: Map<String, List<Reaction>>,
    val reviews: Int,
    var tags: List<String>,
    var title: String,
    val topic: String?,
    val url: String,
    val views: Int,
) {
    var where: String
        get() = fields.field_6
        set(value) {
            fields.field_6 = value
        }

    var who: String
        get() = fields.field_5
        set(value) {
            fields.field_5 = value
        }

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

    fun isResolved(): Boolean {
        return tags.contains("resolved") || fields.isResolved()
    }

    fun isNewLoaf() = fields.isNewLoaf()

    fun isNsfw(): Boolean {
        return tags.contains("nsfw") || fields.isNsfw()

        // We treat every URL as potentially NSFW and warn about that.
        // Warnings from URLs do not apply to the beware itself.
        // || fields.getWhere().contains(Regex("\\(nsfw\\)", RegexOption.IGNORE_CASE))
    }

    fun validate() { // If any of these fire, there may be something I've overseen
        if (description != fields.getDescription()) {
            error("Description field is different than the record description in $this")
        }

        if (title != fields.getTitle()) {
            error("Title field is different than the record title in $this")
        }

        var beware = false
        var caution = false

        if (tags.contains("beware") || isBeware() || title.contains("beware", ignoreCase = true)) {
            beware = true
        }

        if (tags.contains("caution") || !isBeware() || title.contains("caution", ignoreCase = true)) {
            caution = true
        }

        // TODO: Require sync https://github.com/veelkoov/HoldMyAB/issues/2
        // if (beware && caution) {
        //     print("${url}\n${category.name}\n$title | $tags\n\n")
        //     error("The record is a caution, but the title was a beware: $this")
        // }
    }

    override fun toString(): String {
        return "Record[ID=$id]"
    }
}
