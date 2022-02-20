package vlkv.output.pebble

import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate

private class ShorterUrlFilter(vararg val regexes: Pair<Regex, String>) : Filter {
    override fun getArgumentNames(): List<String> {
        return listOf()
    }

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

class Extension : AbstractExtension() {
    override fun getFilters(): Map<String, Filter> {
        return mapOf<String, Filter>(
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

                Regex("^https?://") to "",
                Regex("^([^/]+)/$") to "$1", // Remove trailing slash if there's only domain
            ),
            "shorter_ab_url" to ShorterUrlFilter(
                Regex("^https://artistsbeware.info/beware/.+(/[^/]+/)") to "$1",
            ),
        )
    }
}
