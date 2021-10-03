package vlkv

import vlkv.json.getUniqueNames

class BewareSubject {
    private var names = listOf<String>()
    private val bewares = mutableListOf<Beware>()
    private val where = mutableListOf<String>()

    fun getNames(): List<String> {
        return names.toList()
    }

    fun getBewares(): List<Beware> {
        return bewares.toList()
    }

    fun getWhere(): List<String> {
        return where.toList()
    }

    fun extend(beware: Beware) {
        bewares.add(beware)

        names = getUniqueNames(names.plus(beware.names))

        beware.where.forEach {
            if (!where.contains(it)) {
                where.add(it)
            }
        }
    }
}
