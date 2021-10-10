package vlkv.processing

import vlkv.processing.regexes.Removables
import vlkv.processing.regexes.Replacements
import vlkv.processing.results.StringList

private const val PREFIX_SUBJECTS = "commissioner|client|customer|fursuiter|artist|contest artist participants|resubmit -"

private val TITLE_REMOVABLES = Removables(
    Regex("^($PREFIX_SUBJECTS)? ?(Beware|Caution)[ .:-]*", RegexOption.IGNORE_CASE),
)

private val NAMES_SPLIT = Regex("( - |(?<!u)/|,)")

fun getNames(title: String, who: String): StringList { // TODO: Refactor this crap
    val issues = mutableListOf<String>()
    val titleWithoutPrefix = fixNames(TITLE_REMOVABLES.run(title))

    val names: MutableList<String> = if (titleWithoutPrefix != title) { // PREFIX matched and removed
        NAMES_SPLIT.split(titleWithoutPrefix).map { it.trim() }
    } else {
        issues.add("Nonstandard title, possibly failed to properly handle names")
        listOf(titleWithoutPrefix)
    }.toMutableList()

    names.addAll(NAMES_SPLIT.split(who).map { it.trim() })

    return StringList(names.distinct(), issues)
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
