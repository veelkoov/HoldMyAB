package vlkv.configuration

data class Configuration(
    val ignoredNames: List<String>,
    val ignoredWhere: List<String>,
    val ignoredWhereLines: List<String>,
    val removedTextGeneral: List<String>,
    val ignoredTags: List<String>,
    val nonNameTags: List<String>,
) {
    companion object {
        fun empty() = Configuration(
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
        )
    }
}
