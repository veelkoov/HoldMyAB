package vlkv.output.pebble

import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.extension.escaper.SafeString
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate

private class ShorterUrlFilter(vararg val regexes: Pair<Regex, String>) : Filter {
    override fun getArgumentNames() = listOf<String>()

    override fun apply(input: Any?, args: Map<String, Any>?, self: PebbleTemplate?, context: EvaluationContext?, lineNumber: Int): String {
        if (input !is String) {
            return ""
        }

        var result: String = input

        for ((regex, replacement) in regexes) {
            result = regex.replace(result, replacement)
        }

        return result
    }
}

private class EasyDiffFilter : Filter {
    private val singleNewline = Regex("[\\n ]*\\n +")
    private val indentSpaces = 4

    override fun getArgumentNames() = listOf<String>()

    override fun apply(
        input: Any?,
        args: MutableMap<String, Any>?,
        self: PebbleTemplate?,
        context: EvaluationContext?,
        lineNumber: Int
    ): SafeString {
        if (input !is String) {
            return SafeString("")
        }

        val indent = StringBuffer("")
        val output = StringBuffer("")

        for (originalLine in input.split("\n")) {
            val line = originalLine.trimStart()

            if ("" == line) {
                continue
            }

            val closings = line.windowed(2).count { it == "</" }
            val openings = line.count { it == '<' }.minus(closings)

            if (closings > openings) {
                indent.setLength(indent.length - (closings - openings) * indentSpaces)
            }

            output.append(indent)
            output.append(line)
            output.append("\n")

            if (openings > closings) {
                indent.append(" ".repeat((openings - closings) * indentSpaces))
            }
        }

        return SafeString(output.toString())
    }
}

class Extension : AbstractExtension() {
    override fun getFilters(): Map<String, Filter> {
        return mapOf(
            "shorter_url" to ShorterUrlFilter(
                Regex("https://furaffinity\\.net/user/([^/]+)/?($| )") to "FA: $1",
                Regex("https://twitter\\.com/([^/]+)($| )") to "TT: $1",
                Regex("^https://www\\.instagram\\.com/([^/]+)/?$") to "In: $1",
                Regex("^https://www\\.facebook\\.com/([^/]+)/?$") to "FB: $1",

                Regex("^https://aminoapps\\.com/c/fursuit-maker-amino/page/user/([^/]+)(/[^/]+)?$") to "FM Amino: $1",
                Regex("^https://aminoapps\\.com/c/furry-amino/page/user/([^/]+)(/[^/]+)?$") to "F Amino: $1",

                Regex("^https?://([^.]+)\\.deviantart\\.com/$") to "DA: $1",
                Regex("^https?://www\\.deviantart\\.com/([^/]+)/?$") to "DA: $1",

                Regex("^https?://www\\.etsy\\.com/shop/([^/]+)$") to "Etsy: $1",

                Regex("^https://www\\.hentai-foundry\\.com/user/([^/]+)/profile$") to "Hentai Foundry: $1",
                Regex("^https://([^.]+)\\.tumblr\\.com/$") to "Tumblr: $1",

                Regex("^https?://") to "",
                Regex("^([^/]+)/$") to "$1", // Remove trailing slash if there's only domain
            ),
            "shorter_ab_url" to ShorterUrlFilter(
                Regex("^https://artistsbeware.info/beware/.+(/[^/]+/)") to "$1",
            ),
            "easy_diff" to EasyDiffFilter(),
        )
    }
}
