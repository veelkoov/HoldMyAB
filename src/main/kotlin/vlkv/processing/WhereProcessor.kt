package vlkv.processing

import vlkv.processing.regexes.Removables

private val SPLIT_REGEXES = listOf(
    Regex("\n"),
    Regex("[,;] "),
    Regex(" and ", RegexOption.IGNORE_CASE),
    Regex("\\s+(?=https?://)", RegexOption.IGNORE_CASE),
)

private val REMOVABLES = Removables(
    Regex("^(links:|where:)", RegexOption.IGNORE_CASE),
)

fun getTidyWhere(where: String, urlsFromWho: List<String>): List<String> {
    var subject = REMOVABLES.run(where.trim())
    subject = Urls.expand(subject)
    subject = Urls.fix(subject)
    subject = Urls.removeLabels(subject)

    return split(urlsFromWho.plus(subject), SPLIT_REGEXES.toMutableList())
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
