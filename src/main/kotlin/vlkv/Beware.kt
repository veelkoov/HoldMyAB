package vlkv

data class Beware(
    val names: List<String>,
    val urls: List<String>,
    val url: String,
    val resolved: Boolean,
    val isBeware: Boolean,
    val issues: List<String>,
)
