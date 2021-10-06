package vlkv.pebble

import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate

private val SHORTER_URL_REGEXES = mapOf(
    Regex("https://furaffinity\\.net/user/([^/]+)/?($| )") to "FA: $1",
    Regex("https://twitter\\.com/([^/]+)($| )") to "TT: $1",
    Regex("^https://www\\.instagram\\.com/([^/]+)/?$") to "In: $1",
    Regex("^https://www\\.facebook\\.com/([^/]+)/?$") to "FB: $1",

    Regex("^https://aminoapps\\.com/c/fursuit-maker-amino/page/user/([^/]+)(/[^/]+)?$") to "FM Amino: $1",
    Regex("^https://aminoapps\\.com/c/furry-amino/page/user/([^/]+)(/[^/]+)?$") to "F Amino: $1",

    Regex("^https?://([^.]+)\\.deviantart\\.com/$") to "DA: $1",
    Regex("^https?://www\\.deviantart\\.com/([^/]+)/?$") to "DA: $1",
)

private val shorterUrlFilter = object : Filter {
    override fun getArgumentNames(): List<String> {
        return listOf();
    }

    override fun apply(input: Any?, args: Map<String, Any>?, self: PebbleTemplate?, context: EvaluationContext?, lineNumber: Int): String {
        if (input !is String) {
            return "";
        }

        var result: String = input

        for ((regex, replacement) in SHORTER_URL_REGEXES) {
            result = regex.replace(result, replacement)
        }

        return result
    }
}

class Extension : AbstractExtension() {
    override fun getFilters(): Map<String, Filter> {
        return mapOf<String, Filter>("shorter_url" to shorterUrlFilter)
    }
}
