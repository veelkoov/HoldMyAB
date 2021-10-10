package vlkv.processing

class Removables(vararg regexes: Regex): Replacements(
    *(regexes.map { Pair(it, "") }).toTypedArray()
)
