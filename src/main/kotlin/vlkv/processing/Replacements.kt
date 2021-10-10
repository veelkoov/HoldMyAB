package vlkv.processing

class Replacements(vararg val pairs: Pair<Regex, String>) {
    fun run(input: String): String {
        var result = input

        pairs.forEach { result = result.replace(it.first, it.second) }

        return result
    }
}
