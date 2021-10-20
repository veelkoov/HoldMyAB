package vlkv.fixes

import vlkv.fixes.yaml.Fix
import vlkv.fixes.yaml.Fixes
import vlkv.json.Record

class Fixer(private val fixes: Fixes) {
    val ignoredNames = StringList(fixes.ignoredNames)
    val ignoredWhereLines = StringList(fixes.ignoredWhereLines)

    fun fix(record: Record): Record {
        fixes.fixes.forEach { fix: Fix -> fix.apply(record) }

        return record
    }

    fun assertAllDone() {
        fixes.fixes.forEach { it.assertDone() }
    }

    fun getIgnoredWhere(): List<String> {
        return fixes.ignoredWhere.toList()
    }
}
