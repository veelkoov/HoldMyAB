package vlkv.fixes

import vlkv.configuration.Configuration
import vlkv.processing.Urls
import vlkv.processing.regexes.Removables

class AutoWhoFixer(
    private val configuration: Configuration,
) {

    private val NON_INFORMATIVES = Removables(
        Regex("^\\s*(artist|buyer)\\s*(:\\s*|$)", RegexOption.IGNORE_CASE),
    )
    fun fix(input: String): String {
        var result = input

        result = StringFixer.fix(result)
        result = Urls.tidy(result)
        result = NON_INFORMATIVES.run(result)

        return result
    }
}
