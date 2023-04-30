package vlkv.processing.processors

import vlkv.configuration.Configuration
import vlkv.processing.regexes.Removables

private val REMOVABLES = Removables(
    Regex("^(links:|where:)", RegexOption.IGNORE_CASE),
)

class WhereProcessor(
    private val cfg: Configuration,
) {
    fun fix(input: String, issues: MutableList<String>): String {
        val result = REMOVABLES.run(input.trim())

        val items = result
            .split(",", "/", "\n", " and ")
            .map { it.trim().trimEnd('.').lowercase() }
            .filter { it != "" }

        if (cfg.ignoredWhere.containsAll(items)) {
            issues.add("Ignoring where: '$input'")
            return ""
        }

        return result
            .split("\n")
            .map { it.trim() }
            .filter {
                if (cfg.ignoredWhereLines.contains(it)) {
                    issues.add("Ignoring where: '$it'")

                    false
                } else {
                    true
                }
            }
            .joinToString("\n")
    }
}
