package vlkv.processing

import vlkv.processing.regexes.Removables

private val NON_INFORMATIVES = Removables(
    Regex("^\\s*(artist|buyer)\\s*(:\\s*|$)", RegexOption.IGNORE_CASE),
)

@Suppress("UNUSED_PARAMETER") // TODO: Possibly inform about the removal
fun fixWho(input: String, issues: MutableList<String>): String {
    return NON_INFORMATIVES.run(input)
}
