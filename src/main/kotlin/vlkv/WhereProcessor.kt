package vlkv

private val SPLIT_REGEXES = listOf(
    Regex("\n"),
    Regex("\\s+(?=https?://)", RegexOption.IGNORE_CASE),
)

fun getTidyWhere(where: String, urlsFromWho: List<String>): List<String> {
    return split(urlsFromWho.plus(where), SPLIT_REGEXES.toMutableList())
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
            .map { fixUrls(it) }
            .filter { "" != it }

        if (tokenAfterSplit.size > 1) {
            changed = true
        }

        resultTokens.addAll(tokenAfterSplit)
    }

    return split(resultTokens, if (changed) SPLIT_REGEXES.toMutableList() else withRegexes)
}
