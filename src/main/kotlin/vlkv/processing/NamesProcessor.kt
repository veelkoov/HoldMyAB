package vlkv.processing

private val TITLE_PREFIX = Regex("^(Commissioner|Client|Customer|Fursuiter|Artist)? ?(Beware|Caution)[ .:-]*", RegexOption.IGNORE_CASE)
private val NAMES_SPLIT = Regex("( - |(?<!u)/|,)")

fun getNames(title: String, who: String): List<String> {
    val titleWithoutPrefix = fixNames(TITLE_PREFIX.replace(title, ""))

    val names: MutableList<String> = if (titleWithoutPrefix != title) { // PREFIX matched and removed
        NAMES_SPLIT.split(titleWithoutPrefix).map { it.trim() }
        // FIXME: Very risky approach
    } else {
        listOf(titleWithoutPrefix)
    }.toMutableList()

    names.addAll(NAMES_SPLIT.split(who).map { it.trim() })

    return names.distinct()
}

private val NAME_FIX_REGEX = Regex("@/(?=[a-z])", RegexOption.IGNORE_CASE)

fun fixNames(input: String): String {
    return NAME_FIX_REGEX.replace(input, "@")
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
