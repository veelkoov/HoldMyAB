package vlkv.processing

import vlkv.processing.regexes.Removables
import vlkv.processing.regexes.Replacements

object Urls {
    private val URL_UNIFICATIONS = Replacements(
        Regex("(?<=\\s|^)www\\.") to "http://www.",
        Regex("(https?://[^\\s]+),\\s*(https?://[^\\s]+)") to "$1 $2",
        Regex("furaffinity\\.com") to "furaffinity.net",
        Regex("\\?(lang|s|hl|ref|utm_medium)=(en|\\d{2}|pr_profile|copy_link)($|\\s)", RegexOption.MULTILINE) to "",
        Regex("(https?://)?(www\\.)?(?<!forums\\.)furaffi?nity\\.net/") to "https://furaffinity.net/",
        Regex("https?://furaffinity\\.net/user/([^/\\s]+)/?") to "https://furaffinity.net/user/$1/",
        Regex("(https?://)?twitter\\.com/") to "https://twitter.com/",
        Regex("(https?://)?inkbunny\\.net/") to "https://inkbunny.net/",
        Regex("(https?://)?(www\\.|m\\.)?facebook\\.com/") to "https://www.facebook.com/",
        Regex("(https?://)?([^./\\s]+)(?<!www)\\.deviantart\\.com/") to "https://$2.deviantart.com/",

        // Remove www. from www.username.deviantart.com
        Regex("(https?://)?www\\.([^./\\s]+)\\.deviantart\\.com/?") to "https://$2.deviantart.com/",

        Regex("(https?://)?([^./\\s]+)(?<!www)\\.tumblr\\.com/?") to "https://$2.tumblr.com/",
        Regex("(https?://)?(www\\.)?instagram.com/([^/\\s]+)/?") to "https://www.instagram.com/$3/",
    )

    private fun fix(input: String): String {
        return URL_UNIFICATIONS.run(input)
    }

    private val LABELS = Removables(
        Regex("(Deviant ?Art|DA)( Account)? *[-:]? *(?=https://[^.]+\\.deviantart\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Tumblr? *[-:] *(?=https://[^.]+\\.tumblr\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Twitter *[-:] *(?=https://twitter\\.com/)", RegexOption.IGNORE_CASE),
        Regex("YouTube *[-:] *(?=https://www\\.youtube\\.com/)", RegexOption.IGNORE_CASE),
        Regex("(new |old )?(FA|fur ?affinity)( account)? *[-:]? *(?=https://furaffinity\\.net/)", RegexOption.IGNORE_CASE),
        Regex("InkBunny *[-:] *(?=https://inkbunny\\.net/)", RegexOption.IGNORE_CASE),
        Regex("Instagram *[-:] *(?=https://www\\.instagram\\.com/)", RegexOption.IGNORE_CASE),
        Regex("eBay *[-:] *(?=https://www\\.ebay\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Facebook *[-:] *(?=https://www\\.facebook\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Patreon *[-:] *(?=https://www\\.patreon\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Weasyl *[-:] *(?=https://www\\.weasyl\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Toyhou\\.se *[-:] *(?=https://toyhou\\.se/)", RegexOption.IGNORE_CASE),
        Regex("Picarto *[-:] *(?=https://picarto\\.tv/)", RegexOption.IGNORE_CASE),
        Regex("Trello *[-:] *(?=https://trello\\.com/)", RegexOption.IGNORE_CASE),
    )

    private fun removeLabels(input: String): String {
        return LABELS.run(input)
    }

    fun extract(input: String): UrlsExtractionResult {
        val urls = mutableListOf<String>()
        var remaining = input

        Regex("https?://[^\\s]+", RegexOption.IGNORE_CASE).findAll(input).forEach {
            remaining = remaining.replace(it.value, "")
            urls.add(it.value)
        }

        return UrlsExtractionResult(urls.toList(), remaining)
    }

    private val EXPANSIONS = Replacements(
        Regex("([a-z0-9]+) o[fn] DA, FA(?![a-z])", RegexOption.IGNORE_CASE) to "https://furaffinity.net/user/$1/ https://www.deviantart.com/$1",
        Regex("([a-z0-9]+)(@| o[fn] )(FA|Fur ?affinity)(?![a-z])", RegexOption.IGNORE_CASE) to "https://furaffinity.net/user/$1/",
        Regex("([a-z0-9]+)(@| o[fn] )DA(?![a-z])", RegexOption.IGNORE_CASE) to "https://www.deviantart.com/$1",
    )

    private fun expand(input: String): String {
        return EXPANSIONS.run(input)
    }

    internal fun tidy(input: String): String {
        var result = expand(input)
        result = fix(result)
        result = removeLabels(result)

        return result
    }
}
