package vlkv.fixes.yaml

import vlkv.json.Record

class Fix {
    lateinit var `in`: String
    lateinit var change: List<Change>

    fun apply(record: Record) {
        if (`in` != record.url) {
            return
        }

        for (change in this.change) {
            if (change.what == "title") {
                if (change.from == record.title) {
                    record.title = change.to
                    change.done = true
                }
            } else if (change.what == "who") {
                if (change.from == record.fields.getWho()) {
                    record.fields.setWho(change.to)
                    change.done = true
                }
            } else if (change.what == "where") {
                if (change.from == record.fields.getWhere()) {
                    record.fields.setWhere(change.to)
                    change.done = true
                }
            } else {
                error("Unsupported `what` in $`in` fix: '${change.what}'")
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
}
