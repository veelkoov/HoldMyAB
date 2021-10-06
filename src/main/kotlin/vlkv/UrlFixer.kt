package vlkv

private val FUR_AFFINITY_BASE_URL_REGEX = Regex("(https?://)?(www\\.)?(?<!forums\\.)furaffi?nity\\.net/")
private val TWITTER_BASE_URL_REGEX = Regex("(https?://)?twitter\\.com/")

private val FUR_AFFINITY_LABEL_REGEX = Regex("(new |old )?FA( account)? *[:-] *(?=https://furaffinity\\.net/)", RegexOption.IGNORE_CASE)
private val TWITTER_LABEL_REGEX = Regex("Twitter *[:-] *(?=https://twitter\\.com/)")
private val YOUTUBE_LABEL_REGEX = Regex("YouTube *[:-] *(?=https://www\\.youtube\\.com/)")

private val USELESS_PARAMS_REGEX = Regex("\\?(lang|s|hl|ref)=(en|\\d{2}|pr_profile)$")

fun fixUrls(input: String): String {
    return input
        .replace(FUR_AFFINITY_BASE_URL_REGEX, "https://furaffinity.net/")
        .replace(TWITTER_BASE_URL_REGEX, "https://twitter.com/")

        .replace(FUR_AFFINITY_LABEL_REGEX, "")
        .replace(TWITTER_LABEL_REGEX, "")
        .replace(YOUTUBE_LABEL_REGEX, "")

        .replace(USELESS_PARAMS_REGEX, "")
    // TODO: Other
}
