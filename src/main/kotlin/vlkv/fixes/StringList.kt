package vlkv.fixes

class StringList(private val list: List<String>) {
    private val encountered = mutableListOf<String>()

    fun has(item: String): Boolean {
        return if (list.contains(item.lowercase())) {
            encountered.add(item.lowercase())

            true
        } else {
            false
        }
    }

    fun getUnusedList(): String {
        return list.filterNot { encountered.contains(it) }.joinToString(", ")
    }
}
