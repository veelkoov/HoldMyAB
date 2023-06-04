package vlkv.processing

import vlkv.processing.regexes.Removables
import vlkv.processing.regexes.Replacements
import vlkv.processing.results.UrlsExtractionResult

private val IC = RegexOption.IGNORE_CASE

object Urls {
    private val URL_UNIFICATIONS = Replacements(
        // Generic
        Regex("(?<=\\s|^)www\\.") to "http://www.",
        Regex("(https?://\\S+),\\s*(https?://\\S+)") to "$1 $2",

        // Random parameters
        Regex("\\?(lang|s|hl|ref|utm_medium)=(en|\\d{2}|pr_profile|copy_link|yr_purchases)(&t=[a-zA-Z0-9]+)?($|\\s)", RegexOption.MULTILINE) to "",

        // Remove www. from www.username.deviantart.com
        Regex("(https?://)?www\\.([^./\\s]+)\\.deviantart\\.com/?", IC) to "https://$2.deviantart.com/",
        // DeviantArt
        Regex("(https?://)?([^./\\s]+)(?<!www)\\.deviantart\\.com/?", IC) to "https://$2.deviantart.com/",

        // Etsy
        Regex("(https?://)?www\\.etsy\\.com/([a-z]{2}/)?shop/([^/\\s]+)/?") to "https://www.etsy.com/shop/$3",

        // Facebook
        Regex("(https?://)?(www\\.|m\\.)?facebook\\.com/") to "https://www.facebook.com/",

        // Fur Affinity
        Regex("furaffinity\\.com") to "furaffinity.net",
        Regex("(https?://)?(www\\.)?(?<!forums\\.)furaffi?nity\\.net/") to "https://furaffinity.net/",
        Regex("https?://furaffinity\\.net/(user|gallery)/([^,/\\s]+)/?") to "https://furaffinity.net/user/$2/",

        // Hentai Foundry
        Regex("(https?://)?(www\\.)?hentai-foundry\\.com/profile-([^.]+)\\.php") to "https://www.hentai-foundry.com/user/$3/profile",
        Regex("(https?://)?(www\\.)?hentai-foundry\\.com/user/([^,/\\s]+)(/profile)?") to "https://www.hentai-foundry.com/user/$3/profile",
        Regex("(https?://)?(www\\.)?hentai-foundry\\.com/pictures/user/([^,\\s]+)") to "https://www.hentai-foundry.com/user/$3/profile",

        // Inkbunny
        Regex("(https?://)?inkbunny\\.net/") to "https://inkbunny.net/",

        // Instagram
        Regex("(https?://)?(www\\.)?instagram.com/([^/\\s]+)/?") to "https://www.instagram.com/$3/",

        // Patreon
        Regex("(https?://)?(www\\.)?patreon.com/") to "https://www.patreon.com/",

        // Tumblr
        Regex("(https?://)?([^./\\s]+)(?<!www)\\.tumblr\\.com/?") to "https://$2.tumblr.com/",

        // Twitter
        Regex("(https?://)?(mobile\\.|www\\.)?twitter\\.com/@?", IC) to "https://twitter.com/",
        Regex("(https://twitter.com/[^ \n]+)/with_replies", IC) to "$1",
        Regex("(https://twitter.com/[^ \n]+)\\?[a-z0-9&=_]*", IC) to "$1", // Remove query
    )

    private val LABELS = Removables(
        Regex("(Deviant ?Art|DA)( Account)? *[-:]? *(?=https://[^.]+\\.deviantart\\.com/)", IC),
        Regex("Tumblr? *[-:] *(?=https://[^.]+\\.tumblr\\.com/)", IC),
        Regex("Twitter( alt)? *[-:] *(?=https://twitter\\.com/)", IC),
        Regex("YouTube *[-:] *(?=https://www\\.youtube\\.com/)", IC),
        Regex("(new |old )?(FA|fur ?affinity)( account)? *[-:]? *(?=https://furaffinity\\.net/)", IC),
        Regex("InkBunny *[-:] *(?=https://inkbunny\\.net/)", IC),
        Regex("Instagram *[-:] *(?=https://www\\.instagram\\.com/)", IC),
        Regex("eBay *[-:] *(?=https://www\\.ebay\\.com/)", IC),
        Regex("Etsy *[-:] *(?=https://www\\.etsy\\.com/)", IC),
        Regex("Facebook *[-:] *(?=https://www\\.facebook\\.com/)", IC),
        Regex("Patreon *[-:] *(?=https://www\\.patreon\\.com/)", IC),
        Regex("Weasyl *[-:] *(?=https://www\\.weasyl\\.com/)", IC),
        Regex("Toyhou\\.se *[-:] *(?=https://toyhou\\.se/)", IC),
        Regex("Picarto *[-:] *(?=https://picarto\\.tv/)", IC),
        Regex("Trello *[-:] *(?=https://trello\\.com/)", IC),
        Regex("(Personal site|website) *[-:] *(?=https://)", IC),
    )

    private val EXPANSIONS = Replacements(
        Regex("([a-z0-9]+) o[fn] DA, FA(?![a-z])", IC) to "https://furaffinity.net/user/$1/ https://www.deviantart.com/$1",
        Regex("([a-z0-9]+)(@| o[fn] )(FA|Fur ?affinity)(?![a-z])", IC) to "https://furaffinity.net/user/$1/",
        Regex("([a-z0-9]+)(@| o[fn] )DA(?![a-z])", IC) to "https://www.deviantart.com/$1",
        Regex("@?([a-z0-9_]+)(@| o[fn] )Twitter", IC) to "https://twitter.com/$1",
    )

    private val USERNAMES = listOf(
        Regex("https://([^.]+)\\.deviantart\\.com/", IC),
        Regex("https://www\\.deviantart\\.com/([^/]+)/?", IC),
        Regex("https://furaffinity\\.net/user/([^/]+)/", IC),
        Regex("https://www\\.hentai-foundry\\.com/user/([^/]+)/profile", IC),
        Regex("https://www\\.instagram\\.com/([^/]+)/", IC),
        Regex("https://([^.]+)\\.tumblr\\.com/", IC),
        Regex("https://twitter\\.com/([^/]+)/?", IC),
    )

    fun extract(input: String): UrlsExtractionResult {
        val urls = mutableListOf<String>()
        var remaining = input

        Regex("https?://[^\\s,;]+", IC).findAll(input).forEach {
            remaining = remaining.replaceFirst(it.value, "")
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
