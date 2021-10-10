package vlkv.processing

data class UrlsExtractionResult(
    val urls: List<String>,
    val remaining: String,
)
