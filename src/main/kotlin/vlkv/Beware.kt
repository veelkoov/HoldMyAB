package vlkv

data class Beware(
    val names: List<String>,
    val where: List<String>,
    val url: String,
    val resolved: Boolean,
    val isBeware: Boolean,
)
