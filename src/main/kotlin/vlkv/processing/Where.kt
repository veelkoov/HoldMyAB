package vlkv.processing

import vlkv.processing.regexes.Removables

private val REMOVABLES = Removables(
    Regex("^(links:|where:)", RegexOption.IGNORE_CASE),
)

private val NON_INFORMATIVES = setOf(
    "art commissions discord", // Possibly by fixes.yaml
    "discord",
    "deviantart",
    "deviant art",
    "email",
    "etsy",
    "facebook",
    "fur affinity",
    "furaffinity",
    "furaffinity.net",
    "furry amino",
    "instagram",
    "paypal",
    "secondlife",
    "telegram",
    "toyhouse",
    "trello",
    "tumblr",
    "twitter",
    "www.thedealersden.com",
)

fun fixWhere(input: String, issues: MutableList<String>): String {
    val result = REMOVABLES.run(input.trim())

    val items = result
        .split(",", "/", "\n", " and ")
        .map { it.trim().trimEnd('.').lowercase() }
        .filter { it != "" }

    if (NON_INFORMATIVES.containsAll(items)) {
        issues.add("Ignoring where: '$input'")

        return ""
    }

    return result
}
