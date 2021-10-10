package vlkv

import vlkv.processing.getUniqueNames

class BewareSubject {
    private var names = listOf<String>()
    private val bewares = mutableListOf<Beware>()
    private val where = mutableListOf<String>()
    private val issues = mutableListOf<String>()

    fun getNames(): List<String> {
        return names.toList()
    }

    fun getBewares(): List<Beware> {
        return bewares.toList()
    }

    fun getWhere(): List<String> {
        return where.toList()
    }

    fun getIssues(): List<String> {
        return issues.toList()
    }

    fun extend(beware: Beware) {
        bewares.add(beware)

        names = getUniqueNames(names.plus(beware.names))

        beware.where.forEach {
            if (!where.contains(it)) {
                where.add(it)
            }
        }

        issues.addAll(beware.issues)
    }
}
