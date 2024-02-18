package vlkv.fixes

import vlkv.configuration.Configuration
import vlkv.input.json.Record
import vlkv.processing.Urls
import vlkv.processing.regexes.Removables

class AutoFixer(
    private val configuration: Configuration,
) {
    private val whereFixer = AutoWhereFixer(configuration)
    private val titleFixer = AutoTitleFixer(configuration)
    private val whoFixer = AutoWhoFixer(configuration)

    fun fix(records: List<Record>): List<Record> {
        return records.map { fix(it) }
    }

    fun fix(record: Record): Record {
        record.title = titleFixer.fix(record.title)
        record.where = whereFixer.fix(record.where)
        record.who = whoFixer.fix(record.who)

        return record
    }
}
