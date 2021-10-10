package vlkv.processing

import vlkv.Beware
import vlkv.json.Record
import vlkv.processing.results.NamesUrls

private const val TAG_RESOLVED = "resolved"

fun recordToBeware(record: Record): Beware {
    validate(record)

    val names = mutableListOf<String>()
    val urls = mutableListOf<String>()
    val issues = mutableListOf<String>()

    val fixedTitle = fixTitle(record.title)
    issues.addAll(fixedTitle.issues)

    extend(names, urls, issues, fixedTitle.result)
    extend(names, urls, issues, record.fields.getWho())
    extend(names, urls, issues, fixWhere(record.fields.getWhere()))

    val isResolved = isResolved(record)
    val isBeware = isBeware(record)

    return Beware(record.id, names.distinct(), urls, record.url, isResolved, isBeware, issues)
}

private fun extend(names: MutableList<String>, urls: MutableList<String>, issues: MutableList<String>, input: String) {
    val (newNames, newUrls, newIssues) = getNamesUrls(input)

    names.addAll(newNames)
    urls.addAll(newUrls)
    issues.addAll(newIssues)
}

fun getNamesUrls(input: String): NamesUrls {
    val (urls, remaining) = Urls.extract(Urls.tidy(input))
    val names = getNames(remaining)

    return NamesUrls(names.result, urls, names.issues)
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
