package vlkv.fixes

class StringList(
    input: List<String>,
    private val caseInsensitive: Boolean,
) {
    private val stringsNormToOrig: Map<String, String>
    private val encountered = mutableSetOf<String>()

    init {
        stringsNormToOrig = input.associateBy { normalized(it) }
    }

    private fun normalized(input: String): String {
        return if (caseInsensitive) {
            input.lowercase()
        } else {
            input
        }
    }

    fun contains(checkFor: String): Boolean {
        val matched = stringsNormToOrig[normalized(checkFor)]

        return if (null != matched) {
            encountered.add(matched)

            true
        } else {
            false
        }
    }

    fun containsAll(checkFor: List<String>): Boolean {
        val checkForNormalized = checkFor.map(this::normalized)

        return if (stringsNormToOrig.keys.containsAll(checkForNormalized)) {
            encountered.addAll(checkForNormalized.map { stringsNormToOrig[it]!! })

            true
        } else {
            false
        }
    }

    fun removeFrom(input: String): String {
        if (caseInsensitive) {
            error("Case insensitive list doesn't support removal")
        }

        var result = input

        stringsNormToOrig.values.forEach {
            val afterReplace = result.replace(it, "")

            if (afterReplace != result) {
                result = afterReplace
                encountered.add(it)
            }
        }

        return result
    }

    fun getUnusedList(): String {
        return stringsNormToOrig.values
            .filterNot { encountered.contains(it) }
            .joinToString(", ")
    }

    companion object {
        fun empty() = StringList(listOf(), false)
    }
}
