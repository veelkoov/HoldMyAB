package vlkv.processing

import vlkv.Beware
import vlkv.fixes.Fixer
import vlkv.input.json.Record
import vlkv.processing.results.NamesUrls

private val BRACKETS = Regex("\\(([^)]*)\\)")
private val TAG_MAP = mapOf(
    "artist" to "artist",
    "client" to "client",
    "fursuit" to "fursuit",
)

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
            record.isResolved(),
            record.isNsfw(),
            record.isBeware(),
            issues,
            getTags(record),
        )
    }

    private fun getTags(record: Record): List<String> {
        return record.tags
            .filter { TAG_MAP.keys.contains(it) }
            .map { TAG_MAP[it]!! }
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

    private fun processBrackets(input: String): String {
        return input.replace(BRACKETS, " `ob` $1 `cb`")
    }
}
