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
    "discord",
    "fa)",
    "fa?",
    "facebook",
    "furry amino:",
    "newgrounds:",
    "on discord",
    "on furry amino:",
    "or",
    "other social media",
    "payment done with paypal.",
    "paypal.com",
    "pixiv:",
    "t.me",
    "telegram channel",
    "telegram updates:",
    "telegram",
    "twitter alt:",
    "twitter",
    "twitter.com",
    "website:",
)
private val usedInvalidNames = mutableSetOf<String>()

fun getNames(input: String): StringList {
    val invalidNames = mutableListOf<String>()

    val names = NAMES_SPLIT
        .split(NAME_REPLACEMENTS.run(input))
        .map { it.trim() }
        .filterNot { it == "" }
        .filter {
            if (INVALID_NAMES.contains(it.lowercase())) {
                usedInvalidNames.add(it.lowercase())
                invalidNames.add(it)
                false
            } else {
                true
            }
        }
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

fun getUnusedInvalidNames(): List<String> {
    return INVALID_NAMES.filterNot { usedInvalidNames.contains(it) }
}
