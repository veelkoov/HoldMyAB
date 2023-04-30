package vlkv.fixes

import vlkv.configuration.Fix
import vlkv.configuration.DataFixes
import vlkv.configuration.Configuration
import vlkv.input.json.Record

class Fixer(
    private val dataFixes: DataFixes,
    private val configuration: Configuration,
) {
    fun fix(records: List<Record>): List<Record> {
        return records.map { fix(it) }
    }

    private fun fix(record: Record): Record {
        resolveHtmlEntities(record)
        record.validate()

        dataFixes.fixes.forEach { fix: Fix ->
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

    fun assertAllDone() { // TODO: Find better place for this
        dataFixes.fixes.forEach { it.assertDone() }
    }

    private fun getFixedString(input: String): String {
        return configuration.removedTextGeneral.removeFrom(input)
    }
}
