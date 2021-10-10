package vlkv.processing

import vlkv.processing.regexes.Removables
import vlkv.processing.results.StringResult

private const val PREFIX_SUBJECTS = "commissioner|client|customer|fursuiter|artist|contest artist participants|resubmit -"

private val REMOVABLES = Removables(
    Regex("^($PREFIX_SUBJECTS)? ?(Beware|Caution)[ .:-]*", RegexOption.IGNORE_CASE),
)

fun fixTitle(input: String): StringResult {
    val result = REMOVABLES.run(input)
    val issues: List<String> = if (result == input)
        listOf("Nonstandard title, possibly failed to properly handle names") else listOf()

    return StringResult(result, issues)
}
