package vlkv.processing.regexes

class Removables(vararg regexes: Regex): Replacements(
    *(regexes.map { Pair(it, "") }).toTypedArray()
)
