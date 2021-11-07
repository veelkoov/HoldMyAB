package vlkv.processing

import vlkv.Beware
import vlkv.fixes.Fixer
import vlkv.input.json.Record
import vlkv.processing.results.NamesUrls

private const val TAG_RESOLVED = "resolved"
private const val TAG_NSFW = "nsfw"

class RecordProcessor(fixer: Fixer) {
    private val names = NamesProcessor(fixer)
    private val where = WhereProcessor(fixer)
    private val tags = TagsProcessor(fixer)

    fun getBewares(records: List<Record>): List<Beware> {
        return records.map{ getBeware(it) }
    }

    private fun getBeware(record: Record): Beware {
        val names = mutableListOf<String>()
        val urls = mutableListOf<String>()
        val issues = mutableListOf<String>()

        val fixedTitle = getFixedTitle(record)
        issues.addAll(fixedTitle.issues)

        extend(names, urls, issues, fixedTitle.result)
        extend(names, urls, issues, fixWho(record.fields.getWho(), issues))
        extend(names, urls, issues, where.fix(record.fields.getWhere(), issues))
        names.addAll(tags.filter(record.tags))

        return Beware(
            record.id,
            names.distinct(),
            urls,
            record.url,
            isResolved(record),
            isNsfw(record),
            record.isBeware(),
            issues
        )
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

    private fun isResolved(record: Record): Boolean {
        return record.tags.contains(TAG_RESOLVED) || record.fields.isResolved()
    }

    private fun isNsfw(record: Record): Boolean {
        return record.tags.contains(TAG_NSFW) || record.fields.isNsfw()
    }

    private val BRACKETS = Regex("\\(([^)]*)\\)")

    private fun processBrackets(input: String): String {
        return input.replace(BRACKETS, " `ob` $1 `cb`")
    }
}
