package vlkv.input

import kotlinx.serialization.SerializationException
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
                .forEach(result::add)
        }

    if (result.isEmpty()) {
        throw RuntimeException("No input records found. Expected JSONs from " +
                "artistsbeware.info REST GET /cms/records/ endpoint output in '$inputDirectoryPath'.")
    }

    return result.toList()
}

private fun getRecordsPageFromJsonFile(it: File): RecordsPage {
    try {
        var fixedJson = it.readText().replace("\"reactions\": []", "\"reactions\": {}")

        if (!fixedJson.contains("\"field_40\":")) {
            fixedJson = fixedJson.replace("\"field_28\":", "\"field_40\": null, \"field_28\":")
        }

        return Json.decodeFromString(fixedJson)
    } catch (ex: SerializationException) {
        throw RuntimeException("Failed to parse file ${it.name}", ex)
    }
}

private fun isJsonFile(file: File) = file.isFile && file.name.endsWith(".json")

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
