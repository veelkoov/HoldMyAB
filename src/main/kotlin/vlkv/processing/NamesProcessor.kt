package vlkv.processing

import vlkv.processing.regexes.Replacements

private val NAMES_SPLIT = Regex("( - |(?<!u)/|,)")

fun getNames(title: String, who: String): List<String> {
    var wipTitle = Urls.expand(title)
    wipTitle = fixNames(wipTitle)

    val names = NAMES_SPLIT.split(wipTitle).map { it.trim() }.toMutableList()

    names.addAll(NAMES_SPLIT.split(who).map { it.trim() })

    return names.distinct()
}

private val NAME_REPLACEMENTS = Replacements(
    Regex("@/(?=[a-z])", RegexOption.IGNORE_CASE) to "@",
)

fun fixNames(input: String): String {
    return NAME_REPLACEMENTS.run(input)
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
