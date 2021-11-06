package vlkv.processing

import vlkv.fixes.Fixer

private val IGNORED_TAGS = hashSetOf(
    "2016",
    "2019",
    "2020",
    "adoptable",
    "animation",
    "archive:2011",
    "archive:2012",
    "archives:2011",
    "aritst", // TODO: Typo
    "art trade",
    "artist",
    "beware",
    "beware",
    "caution",
    "chargeback",
    "cilent", // TODO: Typo
    "client",
    "closed species",
    "comimssion", // TODO: Typo
    "commission",
    "commissioner",
    "company",
    "contest",
    "double sale",
    "etsy",
    "frequent name changes",
    "fursuit",
    "inactiveaccount",
    "items",
    "nsfw",
    "other",
    "plush",
    "repost",
    "resolved",
    "sculpture",
    "tracing",
    "trade",
    "writer",
    "ych",
)

class TagsProcessor(val config: Fixer) {
    fun filter(input: List<String>): List<String> {
        return input.filterNot { IGNORED_TAGS.contains(it) }
    }
}
