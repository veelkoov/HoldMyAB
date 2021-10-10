package vlkv.processing

import vlkv.processing.regexes.Replacements
import vlkv.processing.results.StringList

private val NAME_REPLACEMENTS = Replacements(
    Regex("@[/\\\\](?=[a-z])", RegexOption.IGNORE_CASE) to "@",
)

private val NAMES_SPLIT = Regex("( - |(?<!u)/|,|\n)")
private val INVALID_NAMES = listOf( // FIXME: This list would not be needed if the processing was flawless
    "(deleted)",
    "(other aliases)",
    "(private)",
    ";",
    "also",
    "and",
    "artist",
    "buyer",
    "da fa",
    "deviant art:",
    "deviantart",
    "discord",
    "ebay:",
    "email",
    "fa",
    "fa)",
    "fa?",
    "facebook",
    "fur affinity",
    "fur affinity:",
    "furaffinity",
    "furaffinity:",
    "furry amino",
    "inkbunny:",
    "instagram and paypal",
    "instagram",
    "newgrounds:",
    "on discord",
    "on furry amino:",
    "or",
    "other social media",
    "patreon:",
    "payment done with paypal.",
    "paypal.com",
    "picarto:",
    "pixiv:",
    "t.me",
    "telegram channel",
    "telegram updates:",
    "telegram",
    "trello.",
    "trello:",
    "tumblr",
    "tumblr:",
    "twitter alt:",
    "twitter",
    "twitter.com",
    "weasyl:",
    "website:",
)

fun getNames(input: String): StringList {
    val invalidNames = mutableListOf<String>()

    val names = NAMES_SPLIT
        .split(NAME_REPLACEMENTS.run(input))
        .map { it.trim() }
        .filterNot { it == "" }
        .filter { if (INVALID_NAMES.contains(it.lowercase())) { invalidNames.add(it.lowercase()); false } else true }
        .toMutableList()

    val issues: List<String> = if (invalidNames.isEmpty()) listOf() else listOf("Filtered out names: " + invalidNames.joinToString(", "))

    return StringList(names, issues)
}

fun getUniqueNames(names: List<String>): List<String> {
    val result = mutableMapOf<String, String>()

    names.forEach {
        val lc = it.lowercase()

        if (!result.containsKey(lc)) {
            result[lc] = it
        } else {
            val prevVal = getVal(result.getOrDefault(lc, ""))
            val newVal = getVal(it)

            if (newVal < prevVal) { // More capital letters = probably more accurate writing
                result[lc] = it
            }
        }
    }

    return result.values.toList()
}

fun getVal(string: String): Int {
    return string.toByteArray().map { it.toInt() }.reduceOrNull { i1, i2 -> i1 + i2 } ?: 0
}
