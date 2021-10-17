package vlkv.processing

import vlkv.fixes.Fixer
import vlkv.processing.regexes.Replacements
import vlkv.processing.results.StringList

private val NAME_REPLACEMENTS = Replacements(
    Regex("@[/\\\\](?=[a-z])", RegexOption.IGNORE_CASE) to "@",
)

private val NAMES_SPLIT = Regex("( - |(?<!u)/|,|\n)")

class NamesProcessor(private val fixer: Fixer) {
    fun getNames(input: String): StringList {
        val invalidNames = mutableListOf<String>()

        val names = NAMES_SPLIT
            .split(NAME_REPLACEMENTS.run(input))
            .map { it.trim() }
            .filterNot { it == "" }
            .filter {
                if (fixer.isIgnoredName(it)) {
                    invalidNames.add(it)

                    false
                } else {
                    true
                }
            }
            .toMutableList()

        val issues: List<String> = if (invalidNames.isEmpty()) {
            listOf()
        } else {
            listOf("Filtered out names: " + invalidNames.joinToString(", "))
        }

        return StringList(names, issues)
    }
}
