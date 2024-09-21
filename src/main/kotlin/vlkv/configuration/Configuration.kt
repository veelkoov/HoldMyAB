package vlkv.configuration

import vlkv.fixes.StringList

data class Configuration(
    val ignoredNames: StringList,
    val ignoredWhereLines: StringList,
    val removedTextGeneral: StringList,
    val ignoredTags: StringList,
    val nonNameTags: StringList,
    val titlePrefixStripRegexCi: String,
) {
    constructor(
        ignoredNamesCI: List<String>,
        ignoredWhereLines: List<String>,
        removedTextGeneral: List<String>,
        ignoredTags: List<String>,
        nonNameTags: List<String>,
        titlePrefixStripRegexCi: String,
    ) : this(
        StringList(ignoredNamesCI, true),
        StringList(ignoredWhereLines, false),
        StringList(removedTextGeneral, false),
        StringList(ignoredTags, false),
        StringList(nonNameTags, false),
        titlePrefixStripRegexCi,
    )

    companion object {
        fun empty() = Configuration(
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            "(?=a)a", // Never matches
        )
    }
}
