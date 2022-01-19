package vlkv.processing

import vlkv.processing.regexes.Removables
import vlkv.processing.regexes.Replacements

object Urls {
    private val URL_UNIFICATIONS = Replacements(
        Regex("(?<=\\s|^)www\\.") to "http://www.",
        Regex("(https?://[^\\s]+),\\s*(https?://[^\\s]+)") to "$1 $2",
        Regex("furaffinity\\.com") to "furaffinity.net",
        Regex("\\?(lang|s|hl|ref|utm_medium)=(en|\\d{2}|pr_profile|copy_link|yr_purchases)($|\\s)", RegexOption.MULTILINE) to "",
        Regex("(https?://)?(www\\.)?(?<!forums\\.)furaffi?nity\\.net/") to "https://furaffinity.net/",
        Regex("https?://furaffinity\\.net/user/([^/\\s]+)/?") to "https://furaffinity.net/user/$1/",
        Regex("(https?://)?(mobile\\.)?twitter\\.com/") to "https://twitter.com/",
        Regex("(https?://)?inkbunny\\.net/") to "https://inkbunny.net/",
        Regex("(https?://)?(www\\.|m\\.)?facebook\\.com/") to "https://www.facebook.com/",
        Regex("(https?://)?([^./\\s]+)(?<!www)\\.deviantart\\.com/") to "https://$2.deviantart.com/",

        Regex("(https?://)?www\\.etsy\\.com/([a-z]{2}/)?shop/([^/\\s]+)/?") to "https://www.etsy.com/shop/$3",

        // Remove www. from www.username.deviantart.com
        Regex("(https?://)?www\\.([^./\\s]+)\\.deviantart\\.com/?") to "https://$2.deviantart.com/",

        Regex("(https?://)?([^./\\s]+)(?<!www)\\.tumblr\\.com/?") to "https://$2.tumblr.com/",
        Regex("(https?://)?(www\\.)?instagram.com/([^/\\s]+)/?") to "https://www.instagram.com/$3/",
    )

    private val LABELS = Removables(
        Regex("(Deviant ?Art|DA)( Account)? *[-:]? *(?=https://[^.]+\\.deviantart\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Tumblr? *[-:] *(?=https://[^.]+\\.tumblr\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Twitter( alt)? *[-:] *(?=https://twitter\\.com/)", RegexOption.IGNORE_CASE),
        Regex("YouTube *[-:] *(?=https://www\\.youtube\\.com/)", RegexOption.IGNORE_CASE),
        Regex("(new |old )?(FA|fur ?affinity)( account)? *[-:]? *(?=https://furaffinity\\.net/)", RegexOption.IGNORE_CASE),
        Regex("InkBunny *[-:] *(?=https://inkbunny\\.net/)", RegexOption.IGNORE_CASE),
        Regex("Instagram *[-:] *(?=https://www\\.instagram\\.com/)", RegexOption.IGNORE_CASE),
        Regex("eBay *[-:] *(?=https://www\\.ebay\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Etsy *[-:] *(?=https://www\\.etsy\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Facebook *[-:] *(?=https://www\\.facebook\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Patreon *[-:] *(?=https://www\\.patreon\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Weasyl *[-:] *(?=https://www\\.weasyl\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Toyhou\\.se *[-:] *(?=https://toyhou\\.se/)", RegexOption.IGNORE_CASE),
        Regex("Picarto *[-:] *(?=https://picarto\\.tv/)", RegexOption.IGNORE_CASE),
        Regex("Trello *[-:] *(?=https://trello\\.com/)", RegexOption.IGNORE_CASE),
        Regex("(Personal site|website) *[-:] *(?=https://)", RegexOption.IGNORE_CASE),
    )

    private val EXPANSIONS = Replacements(
        Regex("([a-z0-9]+) o[fn] DA, FA(?![a-z])", RegexOption.IGNORE_CASE) to "https://furaffinity.net/user/$1/ https://www.deviantart.com/$1",
        Regex("([a-z0-9]+)(@| o[fn] )(FA|Fur ?affinity)(?![a-z])", RegexOption.IGNORE_CASE) to "https://furaffinity.net/user/$1/",
        Regex("([a-z0-9]+)(@| o[fn] )DA(?![a-z])", RegexOption.IGNORE_CASE) to "https://www.deviantart.com/$1",
        Regex("@?([a-z0-9_]+)(@| o[fn] )Twitter", RegexOption.IGNORE_CASE) to "https://twitter.com/$1",
    )

    private val USERNAMES = listOf(
        Regex("https://twitter\\.com/([^/]+)/?", RegexOption.IGNORE_CASE),
        Regex("https://furaffinity\\.net/user/([^/]+)/", RegexOption.IGNORE_CASE),
        Regex("https://www\\.instagram\\.com/([^/]+)/", RegexOption.IGNORE_CASE),
        Regex("https://([^.]+)\\.deviantart\\.com/", RegexOption.IGNORE_CASE),
        Regex("https://www\\.deviantart\\.com/([^/]+)/?", RegexOption.IGNORE_CASE),
    )

    fun extract(input: String): UrlsExtractionResult {
        val urls = mutableListOf<String>()
        var remaining = input

        Regex("https?://[^\\s]+", RegexOption.IGNORE_CASE).findAll(input).forEach {
            remaining = remaining.replace(it.value, "")
            urls.add(it.value)
        }

        return UrlsExtractionResult(urls.toList(), remaining)
    }

    fun getNamesFromUrls(urls: List<String>): List<String> {
        val names = mutableListOf<String>()

        USERNAMES.forEach { regex ->
            urls.mapNotNull { url -> regex.matchEntire(url)?.groups?.get(1)?.value }
                .forEach { name -> names.add(name) }
        }

        return names
    }

    internal fun tidy(input: String): String {
        var result = EXPANSIONS.run(input)
        result = URL_UNIFICATIONS.run(result)
        result = LABELS.run(result)

        return result
    }
}
