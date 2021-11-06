package vlkv.processing

import vlkv.fixes.Fixer

class TagsProcessor(val fixer: Fixer) {
    fun filter(input: List<String>): List<String> {
        return input.filterNot { fixer.ignoredTags.has(it) }
    }
}
