package vlkv

class Database {
    private val records = HashMap<String, BewareSubject>()

    fun ingest(beware: Beware) {
        val subject = getBewareSubject(beware)

        beware.names.forEach { records[it.lowercase()] = subject }

        subject.extend(beware)
    }

    private fun getBewareSubject(beware: Beware): BewareSubject {
        val existing: List<BewareSubject> = beware.names.mapNotNull { records[it.lowercase()] }.distinct()

        return if (existing.isEmpty()) {
            BewareSubject()
        } else if (existing.count() == 1) {
            existing.single()
        } else {
            error("Shared alias: " + existing.joinToString { it.toString() })
        }
    }

    fun getRecords(): List<BewareSubject> {
        return records.values.distinct().sortedBy { it.getLowestBewareId() }
    }
}
