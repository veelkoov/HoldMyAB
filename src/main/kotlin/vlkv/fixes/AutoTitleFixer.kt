package vlkv.fixes

import vlkv.configuration.Configuration
import vlkv.processing.Urls
import vlkv.processing.regexes.Removables

class AutoTitleFixer(
    val configuration: Configuration,
) {
    fun fix(input: String): String {
        var result = input

        result = StringFixer.fix(result)
        result = removables.run(result)
        result = Urls.tidy(result)

        return result
    }

    private val prefixSubjects = "commissioner|client|customer|fursuiter|artist|contest artist participants|resubmit -"
    private val removables = Removables(Regex("^($prefixSubjects)? ?(Beware|Caution)[ .:!-]*", RegexOption.IGNORE_CASE))
}
