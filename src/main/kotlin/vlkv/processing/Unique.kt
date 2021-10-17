package vlkv.processing

fun getUniqueStrings(names: List<String>): List<String> {
    val result = mutableMapOf<String, String>()

    names.forEach {
        val lc = it.lowercase()

        if (!result.containsKey(lc)) {
            result[lc] = it
        } else {
            val prevRating = getStringRating(result.getOrDefault(lc, ""))
            val newRating = getStringRating(it)

            if (newRating < prevRating) { // More capital letters = probably more accurate writing
                result[lc] = it
            }
        }
    }

    return result.values.toList()
}

private fun getStringRating(string: String): Int {
    return string.toByteArray().map { it.toInt() }.reduceOrNull { i1, i2 -> i1 + i2 } ?: 0
}
