package vlkv.fixes.yaml

import vlkv.fixes.TitleManipulator
import vlkv.fixes.WhereManipulator
import vlkv.fixes.WhoManipulator
import vlkv.json.Record

class Fix {
    lateinit var `in`: String
    lateinit var change: List<Change>

    fun apply(record: Record) {
        if (`in` != record.url) {
            return
        }

        for (change in this.change) {
            val manipulator = when (change.what) {
                "title" -> TitleManipulator
                "who" -> WhoManipulator
                "where" -> WhereManipulator
                else -> error("Unsupported `what` in $`in` fix: '${change.what}'")
            }

            val recordValue = normalize(manipulator.get(record))
            val oldValue = normalize(change.from)
            val newValue = normalize(change.to)

            if (oldValue == recordValue) {
                manipulator.set(record, newValue)
                change.done = true
            }
        }
    }

    fun assertDone() {
        return change.forEach {
            if (!it.done) {
                error("Unused change: $`in`, '${it.what}', '${it.from}'")
            }
        }
    }

    private fun normalize(input: String): String {
        return input.replace("\r\n", "\n").trim()
    }
}
