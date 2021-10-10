package vlkv.processing.results

data class StringResult(
    val result: String,
    var issues: List<String> = listOf(),
)
