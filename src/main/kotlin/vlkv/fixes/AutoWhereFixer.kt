package vlkv.fixes

import vlkv.configuration.Configuration
import vlkv.processing.Urls

class AutoWhereFixer(
    private val configuration: Configuration,
) {
    fun fix(input: String): String {
        var result = input

        result = StringFixer.fix(result)
        result = removeUnwantedWhereLines(result)
        result = Urls.tidy(result)
        result = removeUselessWhereFragments(result)
        result = removeUselessWhereWholeParts(result)

        return result
    }

    private fun removeUnwantedWhereLines(input: String): String {
        return input
            .split("\n")
            .map(String::trim)
            .filterNot { configuration.ignoredWhereLines.contains(it) }
            .joinToString("\n")
    }

    private fun removeUselessWhereWholeParts(input: String): String {
        var result = input

        result = uselessWhereWholePartsRegex.replace(result, "")

        return result
    }

    private fun removeUselessWhereFragments(input: String): String {
        var result = input

        result = uselessWhereFragmentsRegex.replace(result, "")

        return result
    }

    private val uselessWhereWholeParts = listOf(
        "(on |via )?discord( messages?| dms?| chat)?",
        "art commissions discord",

        "email\\.?",
        "gmail",

        "Deviant ?Art",
        "fur ?affinity",
        "FA",

        "(on |via )?telegram( channel| updates)",

        "ToyHouse",
        "Furry Amino",
        "Furbuy",
        "Tumblr",
        "paypal",
        "SecondLife",
        "Trello\\.?",

        "Anthrocon",

        "(on |via )?(( and |, )?(Twitter|Tumblr|Furry Amino|Instagram|PayPal|Deviant ?Art))+",
    )

    private val uselessWhereFragments = listOf(
        "^(links:|where:)",

        "now moved to",
        "through",
        "then",
        "https?://www\\.deviantart\\.com/?",
        "https?://discord\\.com/?",
        "https?://www\\.picarto\\.com/?",
        "https?://www\\.thedealersden\\.com/?",
        "https?://www\\.tumblr\\.com/?",
        "https?://www\\.twitter\\.com/?",
        "https?://www\\.reddit\\.com/?",
    )

    private var uselessWhereWholePartsRegex = Regex(
        "(?<=^|, |\n|/) *("
                + uselessWhereWholeParts.joinToString("|") +
                ") *(?=/|, |\n|$)",
        RegexOption.IGNORE_CASE,
    )

    private var uselessWhereFragmentsRegex = Regex(
        "(?<=^|, | |\n)("
                + uselessWhereFragments.joinToString("|") +
                ")(?=, | |\n|$)",
        RegexOption.IGNORE_CASE,
    )
}
