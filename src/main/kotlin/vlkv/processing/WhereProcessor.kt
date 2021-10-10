package vlkv.processing

import vlkv.processing.regexes.Replacements

private val SPLIT_REGEXES = listOf(
    Regex("\n"),
    Regex("[,;] "),
    Regex(" and ", RegexOption.IGNORE_CASE),
    Regex("\\s+(?=https?://)", RegexOption.IGNORE_CASE),
)

private val REPLACEMENTS = Replacements(
    Regex("^(links:|where:)", RegexOption.IGNORE_CASE) to "",
    Regex("([a-z0-9]+) on DA, FA", RegexOption.IGNORE_CASE) to "https://furaffinity.net/user/$1/ https://www.deviantart.com/$1",
    Regex("([a-z0-9]+)@FA", RegexOption.IGNORE_CASE) to "https://furaffinity.net/user/$1/",
)

fun getTidyWhere(where: String, urlsFromWho: List<String>): List<String> {
    val element = REPLACEMENTS.run(where.trim())
    return split(urlsFromWho.plus(Urls.removeLabels(Urls.fix(element))), SPLIT_REGEXES.toMutableList())
}

private fun split(tokens: List<String>, withRegexes: MutableList<Regex>): List<String> {
    val resultTokens = mutableListOf<String>()
    var changed = false

    if (withRegexes.isEmpty()) {
        return tokens
    }

    val regex = withRegexes.removeAt(0)

    tokens.forEach { token ->
        val tokenAfterSplit = token.split(regex)
            .map { it.trim() }
            .map { Urls.fix(it) }
            .filter { "" != it }

        if (tokenAfterSplit.size > 1) {
            changed = true
        }

        resultTokens.addAll(tokenAfterSplit)
    }

    return split(resultTokens, if (changed) SPLIT_REGEXES.toMutableList() else withRegexes)
}
