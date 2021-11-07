package vlkv.input

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import vlkv.input.json.Record
import vlkv.input.json.RecordsPage
import java.io.File

fun readRecordsFrom(inputDirectoryPath: String): List<Record> {
    val result = mutableListOf<Record>()

    File(inputDirectoryPath).walkTopDown()
        .filter { isJsonFile(it) }
        .map { getRecordsPageFromJsonFile(it) }
        .forEach { page ->
            page.results
                .filter { isProperRecord(it) }
                .forEach { record ->
                    validate(record)
                    result.add(record)
                }
        }

    return result.toList()
}

private fun getRecordsPageFromJsonFile(it: File) = Json.decodeFromString<RecordsPage>(it.readText())

private fun isJsonFile(file: File) = file.isFile && file.name.endsWith(".json")

private fun validate(record: Record) { // If any of these fire, there may be something I've overseen
    val description = Regex("<([^>]+) />").replace(record.fields.getDescription(), "<$1>")

    if (description != record.description) {
        error("Description field is different than the record description")
    }

    if (record.fields.getTitle() != record.title) {
        error("Title field is different than the record title")
    }
}

private const val POSITIVE_REVIEW_REGEXP = "[aAbcDeFghiJlMmnNOoprsStuvy]{3,9} Positive Reviews 20\\d\\d"
private const val CATEGORY_ID_HIDDEN = 9

private fun isProperRecord(record: Record): Boolean {
    if (Regex(POSITIVE_REVIEW_REGEXP).matches(record.title)) {
        return false
    }

    if (CATEGORY_ID_HIDDEN == record.category.id) { // Should be already filtered out during fetch
        return false
    }

    return !record.hidden // Should be already filtered out during fetch
}
