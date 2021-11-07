package vlkv.processing

import vlkv.input.json.Record
import vlkv.processing.regexes.Removables
import vlkv.processing.results.StringResult

private const val PREFIX_SUBJECTS = "commissioner|client|customer|fursuiter|artist|contest artist participants|resubmit -"

private val BEWARE_REMOVABLES = Removables(
    Regex("^($PREFIX_SUBJECTS)? ?Beware[ .:!-]*", RegexOption.IGNORE_CASE),
)

private val CAUTION_REMOVABLES = Removables(
    Regex("^($PREFIX_SUBJECTS)? ?Caution[ .:!-]*", RegexOption.IGNORE_CASE),
)

fun getFixedTitle(record: Record): StringResult {
    val midResult = BEWARE_REMOVABLES.run(record.title)
    val issues = mutableListOf<String>()

    if (!record.isBeware() && midResult != record.title) {
        issues.add("The record is a caution, but the title was a beware")
    }

    val result = CAUTION_REMOVABLES.run(midResult)

    if (record.isBeware() && result != midResult) {
        issues.add("The record is a beware, but the title was a caution")
    }

    return StringResult(result, issues)
}
