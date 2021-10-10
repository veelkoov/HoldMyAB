package vlkv.processing

import vlkv.processing.regexes.Removables

private val REMOVABLES = Removables(
    Regex("^(links:|where:)", RegexOption.IGNORE_CASE),
)

fun fixWhere(input: String): String {
    return REMOVABLES.run(input.trim())
}
