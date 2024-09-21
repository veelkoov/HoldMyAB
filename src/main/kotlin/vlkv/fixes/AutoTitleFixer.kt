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

    private val removables = Removables(Regex(configuration.titlePrefixStripRegexCi, RegexOption.IGNORE_CASE))
}
