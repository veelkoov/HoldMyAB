package vlkv.processing

import vlkv.processing.regexes.Removables

private val REMOVABLES = Removables(
    Regex("^(links:|where:)", RegexOption.IGNORE_CASE),
)

private val NON_INFORMATIVES = setOf(
    "discord",
    "email",
    "facebook",
    "fur affinity",
    "furaffinity",
    "furaffinity.net",
    "furry amino",
    "instagram",
    "telegram",
    "toyhouse",
    "trello",
    "tumblr",
    "twitter",
)

fun fixWhere(input: String, issues: MutableList<String>): String {
    val result = REMOVABLES.run(input.trim())

    val items = result
        .split(',', '.', '/')
        .map { it.trim().lowercase() }
        .filter { it != "" }

    if (NON_INFORMATIVES.containsAll(items)) {
        issues.add("Ignoring where: '$input'")

        return ""
    }

    return result
}
