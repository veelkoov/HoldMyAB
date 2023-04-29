package vlkv

data class Beware(
    val id: Int,
    val abUrl: String,
    val names: List<String>,
    val urls: List<String>,
    val resolved: Boolean,
    val isNsfw: Boolean,
    val isBeware: Boolean,
    val issues: List<String>,
    val subjectTags: List<String>,
    val tags: List<String>,
)
