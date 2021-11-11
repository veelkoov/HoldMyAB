package vlkv

data class Beware(
    val id: Int,
    val names: List<String>,
    val urls: List<String>,
    val url: String,
    val resolved: Boolean,
    val isNsfw: Boolean,
    val isBeware: Boolean,
    val issues: List<String>,
    val tags: List<String>,
)
