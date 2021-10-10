package vlkv

import vlkv.processing.getUniqueNames

class BewareSubject {
    private var names = listOf<String>()
    private val bewares = mutableListOf<Beware>()
    private val urls = mutableListOf<String>()
    private val issues = mutableListOf<String>()

    @Suppress("unused") // Pebble uses it
    fun getNames(): List<String> {
        return names.toList()
    }

    @Suppress("unused") // Pebble uses it
    fun getBewares(): List<Beware> {
        return bewares.toList()
    }

    @Suppress("unused") // Pebble uses it
    fun getUrls(): List<String> {
        return urls.toList()
    }

    @Suppress("unused") // Pebble uses it
    fun getIssues(): List<String> {
        return issues.toList()
    }

    fun extend(beware: Beware) {
        bewares.add(beware)

        names = getUniqueNames(names.plus(beware.names))

        beware.urls.forEach {
            if (!urls.contains(it)) {
                urls.add(it)
            }
        }

        issues.addAll(beware.issues)
    }
}
