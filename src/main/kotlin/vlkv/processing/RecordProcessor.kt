package vlkv.processing

import vlkv.Beware
import vlkv.fixes.Fixer
import vlkv.json.Record
import vlkv.processing.results.NamesUrls

private const val TAG_RESOLVED = "resolved"

class RecordProcessor(fixer: Fixer) {
    private val names = NamesProcessor(fixer)
    private val where  = WhereProcessor(fixer)

    fun getBeware(record: Record): Beware {
        val names = mutableListOf<String>()
        val urls = mutableListOf<String>()
        val issues = mutableListOf<String>()

        val fixedTitle = getFixedTitle(record)
        issues.addAll(fixedTitle.issues)

        extend(names, urls, issues, fixedTitle.result)
        extend(names, urls, issues, fixWho(record.fields.getWho(), issues))
        extend(names, urls, issues, where.fix(record.fields.getWhere(), issues))

        val isResolved = isResolved(record)

        return Beware(record.id, names.distinct(), urls, record.url, isResolved, record.isBeware(), issues)
    }

    private fun extend(
        names: MutableList<String>,
        urls: MutableList<String>,
        issues: MutableList<String>,
        input: String
    ) {
        val (newNames, newUrls, newIssues) = getNamesUrls(input)

        names.addAll(newNames)
        urls.addAll(newUrls)
        issues.addAll(newIssues)
    }

    internal fun getNamesUrls(input: String): NamesUrls {
        val withoutBrackets = processBrackets(input)
        val (urls, remaining) = Urls.extract(Urls.tidy(withoutBrackets))
        val names = names.getNames(remaining)
        val namesFromUrls = Urls.getNamesFromUrls(urls)

        return NamesUrls(names.result.plus(namesFromUrls), urls, names.issues)
    }

    internal fun validate(record: Record) { // If any of these fire, there may be something I've overseen
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

    private val BRACKETS = Regex("\\(([^)]*)\\)")

    private fun processBrackets(input: String): String {
        return input.replace(BRACKETS, " `ob` $1 `cb`")
    }
}
