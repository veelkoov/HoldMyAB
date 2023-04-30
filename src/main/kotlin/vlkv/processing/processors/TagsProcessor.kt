package vlkv.processing.processors

import vlkv.configuration.Configuration
import vlkv.input.json.Record

class TagsProcessor(private val configuration: Configuration) {
    fun getNameTags(record: Record): List<String> {
        return record.tags
            .filterNot { configuration.ignoredTags.contains(it) }
            .filterNot { configuration.nonNameTags.contains(it) }
    }

    fun getSubjectTags(record: Record): List<String> {
        return record.tags
            .filterNot { configuration.ignoredTags.contains(it) }
            .filter { configuration.nonNameTags.contains(it) }
    }
}
