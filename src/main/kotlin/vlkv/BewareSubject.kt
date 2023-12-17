package vlkv

import vlkv.processing.getUniqueStrings

class BewareSubject {
    private var names = listOf<String>()
    private var urls = listOf<String>()
    private val tags = mutableSetOf<String>()
    private val bewares = mutableListOf<Beware>()
    private val issues = mutableListOf<String>()

    override fun toString(): String {
        return "BewareSubject(names=$names, urls=$urls, issues=$issues)"
    }

    @Suppress("unused") // Pebble uses it
    fun getNamesSorted(): List<String> {
        return names.toList().sorted()
    }

    @Suppress("unused") // Pebble uses it
    fun getUrlsSorted(): List<String> {
        return urls.toList().sorted()
    }

    @Suppress("unused") // Pebble uses it
    fun getTagsSorted(): List<String> {
        return tags.toList().sorted()
    }

    @Suppress("unused") // Pebble uses it
    fun getBewaresSorted(): List<Beware> {
        return bewares.sortedBy { it.id }
    }

    @Suppress("unused") // Pebble uses it
    fun getIssuesSorted(): List<String> {
        return issues.toList().sorted()
    }

    fun extend(beware: Beware) {
        bewares.add(beware)

        names = getUniqueStrings(names.plus(beware.names))
        urls = getUniqueStrings(urls.plus(beware.urls))
        tags.addAll(beware.subjectTags)

        issues.addAll(beware.issues)
    }

    fun getLowestBewareId(): Int {
        return bewares.minOf { it.id }
    }

    @Suppress("unused") // Pebble uses it
    fun getNewLoaf(): Boolean {
        val isNewLoaf = bewares.any { it.isNewLoaf }
        val isNotNewLoaf = bewares.any { !it.isNewLoaf }

        if (isNewLoaf && isNotNewLoaf) {
            throw IllegalStateException("$this is both New Leaf and not")
        }

        return isNewLoaf
    }
}
