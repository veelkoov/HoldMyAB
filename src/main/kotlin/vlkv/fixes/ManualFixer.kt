package vlkv.fixes

import vlkv.configuration.Fix
import vlkv.configuration.DataFixes
import vlkv.configuration.Configuration
import vlkv.input.json.Record

class ManualFixer(
    private val configuration: Configuration,
    private val dataFixes: DataFixes,
) {
    fun fix(records: List<Record>): List<Record> {
        return records.map { fix(it) }
    }

    private fun fix(record: Record): Record {
        dataFixes.fixes.forEach { fix: Fix ->
            fix.apply(record)

            record.title = getFixedString(record.title)
            record.where = getFixedString(record.where)
            record.who = getFixedString(record.who)
        }

        return record
    }

    fun assertAllDone() { // TODO: Find better place for this
        dataFixes.fixes.forEach { it.assertDone() }
    }

    private fun getFixedString(input: String): String {
        return configuration.removedTextGeneral.removeFrom(input)
    }
}
