package vlkv.processing

import vlkv.processing.regexes.Removables
import vlkv.processing.regexes.Replacements

object Urls {
    private val URL_UNIFICATIONS = Replacements(
        Regex("\\?(lang|s|hl|ref)=(en|\\d{2}|pr_profile)($| )", RegexOption.MULTILINE) to "",
        Regex("(https?://)?(www\\.)?(?<!forums\\.)furaffi?nity\\.net/") to "https://furaffinity.net/",
        Regex("https://furaffinity\\.net/user/([^/]+)/?") to "https://furaffinity.net/user/$1/",
        Regex("(https?://)?twitter\\.com/") to "https://twitter.com/",
        Regex("(https?://)?(www\\.)?facebook\\.com/") to "https://www.facebook.com/",
    )

    fun fix(input: String): String {
        return URL_UNIFICATIONS.run(input)
    }

    private val LABELS = Removables(
        Regex("DeviantArt( Account)? *[-:] *https://[^.]+]\\.deviantart\\.com/", RegexOption.IGNORE_CASE),
        Regex("Twitter *[-:] *(?=https://twitter\\.com/)", RegexOption.IGNORE_CASE),
        Regex("YouTube *[-:] *(?=https://www\\.youtube\\.com/)", RegexOption.IGNORE_CASE),
        Regex("(new |old )?FA( account)? *[-:] *(?=https://furaffinity\\.net/)", RegexOption.IGNORE_CASE),
        Regex("Instagram *[-:] *(?=https://www\\.instagram\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Facebook *[-:] *(?=https://www\\.facebook\\.com/)", RegexOption.IGNORE_CASE),
        Regex("Toyhou\\.se *[-:] *(?=https://toyhou\\.se/)", RegexOption.IGNORE_CASE),
    )

    fun removeLabels(input: String): String {
        return LABELS.run(input)
    }

    fun extract(input: String): UrlsExtractionResult {
        val urls = mutableListOf<String>()
        var remaining = input

        Regex("https?://[^ ]+", RegexOption.IGNORE_CASE).findAll(input).forEach {
            remaining = remaining.replace(it.value, "")
            urls.add(it.value)
        }

        return UrlsExtractionResult(urls.toList(), remaining)
    }
}

