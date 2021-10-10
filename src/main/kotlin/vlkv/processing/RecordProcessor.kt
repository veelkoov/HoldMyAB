package vlkv.processing

import vlkv.Beware
import vlkv.json.Record

private const val TAG_RESOLVED = "resolved"

fun recordToBeware(record: Record): Beware {
    validate(record)

    val (urlsFromWho, whoWithoutUrls) = Urls.extract(record.fields.getWho())
    val fixedTitle = fixTitle(record.title)
    val names = getNames(fixedTitle.result, whoWithoutUrls)
    val where = getTidyWhere(record.fields.getWhere(), urlsFromWho)
    val isResolved = isResolved(record)
    val isBeware = isBeware(record)

    return Beware(names, where, record.url, isResolved, isBeware, fixedTitle.issues) // TODO: Other stuff
}

private fun validate(record: Record) { // If any of these fire, there may be something I've overseen
    val description = Regex("<([^>]+) />").replace(record.fields.getDescription(), "<$1>")

    if (description != record.description) {
        error("Description field is different than the record description")
    }

    if (record.fields.getTitle() != record.title) {
        error("Title field is different than the record title")
    }
}

private fun isResolved(record: Record): Boolean {
    return record.tags.contains(TAG_RESOLVED) || record.fields.isResolved()
}

private fun isBeware(record: Record): Boolean {
    if (record.category.name.contains("beware", true)
        && !record.category.name.contains("caution", true)
    ) {
        return true
    }

    if (record.category.name.contains("caution", true)) {
        return false
    }

    error("Failed to decide if a beware or a caution: " + record.title)
}
