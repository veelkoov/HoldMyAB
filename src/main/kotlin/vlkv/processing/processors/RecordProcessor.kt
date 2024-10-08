package vlkv.processing.processors

import vlkv.Beware
import vlkv.configuration.Configuration
import vlkv.input.json.Record
import vlkv.processing.Urls
import vlkv.processing.results.NamesUrls

private val BRACKETS = Regex("\\(([^)]*)\\)")

class RecordProcessor(
    cfg: Configuration,
) {
    private val namesProcessor = NamesProcessor(cfg)
    private val tagsProcessor = TagsProcessor(cfg)

    fun getBewares(records: List<Record>): List<Beware> {
        return records.map{ getBeware(it) }
    }

    private fun getBeware(record: Record): Beware {
        val names = mutableListOf<String>()
        val urls = mutableListOf<String>()
        val issues = mutableListOf<String>()

        extend(names, urls, issues, record.title)
        extend(names, urls, issues, record.who)
        extend(names, urls, issues, record.where)
        names.addAll(tagsProcessor.getNameTags(record))

        return Beware(
            record.id,
            record.url,
            names.distinct(),
            urls,
            record.isResolved(),
            record.isNsfw(),
            record.isBeware(),
            record.isNewLoaf(),
            issues,
            tagsProcessor.getSubjectTags(record),
            record.tags,
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
        val (urls, remaining) = Urls.extract(withoutBrackets)
        val names = namesProcessor.getNames(remaining)
        val namesFromUrls = Urls.getNamesFromUrls(urls)

        return NamesUrls(names.result.plus(namesFromUrls), urls, names.issues)
    }

    private fun processBrackets(input: String): String {
        return input.replace(BRACKETS, " `ob` $1 `cb`")
    }
}
