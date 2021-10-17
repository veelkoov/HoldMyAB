package vlkv.processing

import vlkv.fixes.Fixer
import vlkv.processing.regexes.Removables

private val REMOVABLES = Removables(
    Regex("^(links:|where:)", RegexOption.IGNORE_CASE),
)

class WhereProcessor(private val fixer: Fixer) {
    fun fix(input: String, issues: MutableList<String>): String {
        val result = REMOVABLES.run(input.trim())

        val items = result
            .split(",", "/", "\n", " and ")
            .map { it.trim().trimEnd('.').lowercase() }
            .filter { it != "" }

        if (fixer.getIgnoredWhere().containsAll(items)) {
            issues.add("Ignoring where: '$input'")
            return ""
        }

        return result
            .split("\n")
            .map { it.trim() }
            .filter {
                if (fixer.ignoredWhereLines.has(it)) {
                    issues.add("Ignoring where: '$it'")

                    false
                } else {
                    true
                }
            }
            .joinToString("\n")
    }
}
