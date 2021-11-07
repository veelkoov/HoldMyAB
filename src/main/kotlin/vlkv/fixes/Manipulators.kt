package vlkv.fixes

import vlkv.fixes.yaml.Change
import vlkv.input.json.Record

interface Manipulator<T> {
    fun get(record: Record): T
    fun set(record: Record, newValue: T)
    fun updateIfMatches(record: Record, change: Change): Boolean
}

abstract class StringManipulator : Manipulator<String> {
    private fun normalize(input: String): String {
        return input.replace("\r\n", "\n").trim()
    }

    override fun updateIfMatches(record: Record, change: Change): Boolean {
        val recordValue = normalize(get(record))
        val oldValue = normalize(change.from)
        val newValue = normalize(change.to)

        return if (recordValue == oldValue) {
            set(record, newValue)
            true
        } else {
            false
        }
    }
}

abstract class ListManipulator : Manipulator<List<String>> {
    override fun updateIfMatches(record: Record, change: Change): Boolean {
        val oldValue = get(record)

        return if (oldValue.contains(change.from)) {
            val newValue = oldValue.toMutableList()
            newValue.remove(change.from)
            newValue.add(change.to)

            set(record, newValue)
            true
        } else {
            false
        }
    }
}

object TitleManipulator : StringManipulator() {
    override fun get(record: Record): String {
        return record.title
    }

    override fun set(record: Record, newValue: String) {
        record.title = newValue
    }
}

object WhoManipulator : StringManipulator() {
    override fun get(record: Record): String {
        return record.fields.getWho()
    }

    override fun set(record: Record, newValue: String) {
        record.fields.setWho(newValue)
    }
}

object WhereManipulator : StringManipulator() {
    override fun get(record: Record): String {
        return record.fields.getWhere()
    }

    override fun set(record: Record, newValue: String) {
        record.fields.setWhere(newValue)
    }
}

object TagManipulator : ListManipulator() {
    override fun get(record: Record): List<String> {
        return record.tags
    }

    override fun set(record: Record, newValue: List<String>) {
        record.tags = newValue
    }
}
