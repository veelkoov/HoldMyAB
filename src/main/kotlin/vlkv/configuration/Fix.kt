package vlkv.configuration

import vlkv.fixes.TagManipulator
import vlkv.fixes.TitleManipulator
import vlkv.fixes.WhereManipulator
import vlkv.fixes.WhoManipulator
import vlkv.input.json.Record

data class Fix (
    val `in`: String,
    val changes: List<Change>,
) {
    fun apply(record: Record) {
        if (`in` != record.url) {
            return
        }

        for (change in this.changes) {
            val manipulator = when (change.what) {
                "title" -> TitleManipulator
                "who" -> WhoManipulator
                "where" -> WhereManipulator
                "tag" -> TagManipulator
                else -> error("Unsupported `what` in $`in` fix: '${change.what}'")
            }

            if (manipulator.updateIfMatches(record, change)) {
                change.done = true
            }
        }
    }

    fun assertDone() {
        return changes.forEach {
            if (!it.done) {
                error("Unused change: $`in`, '${it.what}', '${it.from}'")
            }
        }
    }
}
