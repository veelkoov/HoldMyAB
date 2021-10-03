package vlkv

import vlkv.json.Record
import vlkv.json.getNames

private const val TAG_RESOLVED = "resolved"

fun recordToBeware(record: Record): Beware {
    validate(record)

    val (whoWithoutUrls, urlsFromWho) = extractUrlsFromWho(record.fields.field_5)

    val names = getNames(record.title, whoWithoutUrls)
    val isResolved = isResolved(record)

    val where = getTidyWhere(record.fields.getWhere(), urlsFromWho)

    return Beware(names, where, record.url, isResolved) // TODO: Other stuff
}

private fun extractUrlsFromWho(who: String): Pair<String, List<String>> {
    val urls = mutableListOf<String>()
    var result = who

    Regex("https?://[^ ]+", RegexOption.IGNORE_CASE).findAll(who).forEach {
        result = result.replace(it.value, "")
        urls.add(it.value)
    }

    return result to urls.toList()
}

private fun validate(record: Record) {
    val field4 = Regex("<([^>]+) />").replace(record.fields.field_4, "<$1>")
    val description = record.description

    if (field4 != description) {
        error("field_4 is different that description") // If this fires, there may be something I've overseen
    }
}

private fun isResolved(record: Record): Boolean {
    return record.tags.contains(TAG_RESOLVED) || record.fields.isResolved()
}
