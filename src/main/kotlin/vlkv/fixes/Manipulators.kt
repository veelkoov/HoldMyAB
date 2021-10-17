package vlkv.fixes

import vlkv.json.Record

interface Manipulator {
    fun get(record: Record): String
    fun set(record: Record, newValue: String)
}

object TitleManipulator: Manipulator {
    override fun get(record: Record): String {
        return record.title
    }

    override fun set(record: Record, newValue: String) {
        record.title = newValue
    }
}

object WhoManipulator: Manipulator {
    override fun get(record: Record): String {
        return record.fields.getWho()
    }

    override fun set(record: Record, newValue: String) {
        record.fields.setWho(newValue)
    }
}

object WhereManipulator: Manipulator {
    override fun get(record: Record): String {
        return record.fields.getWhere()
    }

    override fun set(record: Record, newValue: String) {
        record.fields.setWhere(newValue)
    }
}
