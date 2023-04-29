package vlkv.configuration

data class DataFixes(
    val fixes: List<Fix>,
) {
    companion object {
        fun empty() = DataFixes(
            listOf(),
        )
    }
}
