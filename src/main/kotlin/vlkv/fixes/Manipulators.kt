package vlkv.fixes

import vlkv.json.Record

interface Manipulator {
    fun get(record: Record): String
    fun set(record: Record, newValue: String)
    fun normalize(input: String): String
}

abstract class StringManipulator: Manipulator {
    override fun normalize(input: String): String {
        return input.replace("\r\n", "\n").trim()
    }
}

object TitleManipulator: StringManipulator() {
    override fun get(record: Record): String {
        return record.title
    }

    override fun set(record: Record, newValue: String) {
        record.title = newValue
    }
}

object WhoManipulator: StringManipulator() {
    override fun get(record: Record): String {
        return record.fields.getWho()
    }

    override fun set(record: Record, newValue: String) {
        record.fields.setWho(newValue)
    }
}

object WhereManipulator: StringManipulator() {
    override fun get(record: Record): String {
        return record.fields.getWhere()
    }

    override fun set(record: Record, newValue: String) {
        record.fields.setWhere(newValue)
    }
}
