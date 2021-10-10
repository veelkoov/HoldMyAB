package vlkv.processing

import vlkv.processing.regexes.Replacements

private val NAME_REPLACEMENTS = Replacements(
    Regex("@[/\\\\](?=[a-z])", RegexOption.IGNORE_CASE) to "@",
)

private val NAMES_SPLIT = Regex("( - |(?<!u)/|,|\n)")

fun getNames(input: String): List<String> {
    return NAMES_SPLIT
        .split(NAME_REPLACEMENTS.run(input))
        .map { it.trim() }
        .filterNot { it == "" }
        .toMutableList()
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
