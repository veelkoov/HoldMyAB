package vlkv.fixes

import vlkv.fixes.yaml.Fix
import vlkv.fixes.yaml.Fixes
import vlkv.input.json.Record

class Fixer(private val fixes: Fixes) {
    val ignoredNames = StringList(fixes.ignoredNames)
    val ignoredWhereLines = StringList(fixes.ignoredWhereLines)
    val ignoredTags = StringList(fixes.ignoredTags)

    fun fix(records: List<Record>): List<Record> {
        return records.map { fix(it) }
    }

    private fun fix(record: Record): Record {
        resolveHtmlEntities(record)
        record.validate()

        fixes.fixes.forEach { fix: Fix ->
            fix.apply(record)
            record.title = getFixedString(record.title)
            record.fields.setWhere(getFixedString(record.fields.getWhere()))
            record.fields.setWho(getFixedString(record.fields.getWho()))
        }

        return record
    }

    private fun resolveHtmlEntities(record: Record) {
        record.fields.setWhere(resolveHtmlEntities(record.fields.getWhere()))
        record.fields.setWho(resolveHtmlEntities(record.fields.getWho()))
        record.fields.setTitle(resolveHtmlEntities(record.fields.getTitle()))
        record.fields.setDescription(resolveHtmlEntities(record.fields.getDescription()))
        record.description = resolveHtmlEntities(record.description)
    }

    private fun resolveHtmlEntities(input: String): String {
        val result = Regex("<([^>]+) />").replace(input, "<$1>")

        return result.replace("&gt;", ">").replace("&amp;", "&")
    }

    fun assertAllDone() {
        fixes.fixes.forEach { it.assertDone() }
    }

    fun getIgnoredWhere(): List<String> {
        return fixes.ignoredWhere.toList()
    }

    private fun getFixedString(input: String): String {
        var result = input // TODO: Possibly report unused

        fixes.removedTextGeneral.forEach { result = result.replace(it, "") }

        return result
    }
}
