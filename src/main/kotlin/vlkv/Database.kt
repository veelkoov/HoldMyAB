package vlkv

class Database(bewares: List<Beware>) {
    private val records = HashMap<String, BewareSubject>()

    init {
        bewares.forEach { ingest(it) }
    }

    private fun ingest(beware: Beware) {
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
            error("Shared alias: " + existing.joinToString(" AND ") {
                it.getNamesSorted().joinToString(" aka ")
            } + ". URIs: " + existing.joinToString(", ") {
                it.getBewaresSorted().joinToString(", ") { bIt -> bIt.abUrl }
            })
        }
    }

    fun getSortedRecords(): List<BewareSubject> {
        return records.values.distinct().sortedBy { it.getLowestBewareId() }
    }
}
