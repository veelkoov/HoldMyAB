package vlkv.processing

import vlkv.Beware
import vlkv.fixes.Fixer
import vlkv.input.json.Record
import vlkv.processing.results.NamesUrls

private val BRACKETS = Regex("\\(([^)]*)\\)")
private val TAG_MAP = mapOf( // FIXME why these need to albo be in tags ignored?
    "artist" to "artist",
    "client" to "client",
    "fursuit" to "fursuit",
    "kigurumi" to "kigurumi",
    "musician" to "musician",
    "scammer" to "scammer",
    "art theft" to "art theft",
)

class RecordProcessor(fixer: Fixer) {
    private val namesProcessor = NamesProcessor(fixer)
    private val whereProcessor = WhereProcessor(fixer)
    private val tagsProcessor = TagsProcessor(fixer)

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
        extend(names, urls, issues, whereProcessor.fix(record.fields.getWhere(), issues))
        names.addAll(tagsProcessor.filter(record.tags))

        return Beware(
            record.id,
            names.distinct(),
            urls,
            record.url,
            record.isResolved(),
            record.isNsfw(),
            record.isBeware(),
            issues,
            getSubjectTags(record),
            record.tags,
        )
    }

    private fun getSubjectTags(record: Record): List<String> {
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
        val names = namesProcessor.getNames(remaining)
        val namesFromUrls = Urls.getNamesFromUrls(urls)

        return NamesUrls(names.result.plus(namesFromUrls), urls, names.issues)
    }

    private fun processBrackets(input: String): String {
        return input.replace(BRACKETS, " `ob` $1 `cb`")
    }
}
