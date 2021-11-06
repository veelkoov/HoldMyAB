package vlkv.fixes

import vlkv.fixes.yaml.Fix
import vlkv.fixes.yaml.Fixes
import vlkv.json.Record

class Fixer(private val fixes: Fixes) {
    val ignoredNames = StringList(fixes.ignoredNames)
    val ignoredWhereLines = StringList(fixes.ignoredWhereLines)
    val ignoredTags = StringList(fixes.ignoredTags)

    fun fix(record: Record): Record {
        fixes.fixes.forEach { fix: Fix ->
            fix.apply(record)
            record.title = getFixedString(record.title)
            record.fields.setWhere(getFixedString(record.fields.getWhere()))
            record.fields.setWho(getFixedString(record.fields.getWho()))
        }

        return record
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
