package vlkv

import com.mitchellbosecke.pebble.PebbleEngine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import vlkv.json.Record
import vlkv.json.RecordsPage
import java.io.File


fun main(args: Array<String>) {
    if (args.size != 2) {
        error("Require exactly two arguments: 1. path to the input directory, 2. path to the report file to generate")
    }

    val inputDirPath = args[0]
    val outputFilePath = args[1]

    val database = readInputFilesFromDir(inputDirPath)
    renderToFile(database, outputFilePath)

    println("Finished!")
}

private fun readInputFilesFromDir(inputDirPath: String): Database {
    val database = Database()

    File(inputDirPath).walkTopDown()
        .filter { it.isFile && it.name.endsWith(".json") }
        .map { Json.decodeFromString<RecordsPage>(it.readText()) }
        .forEach { page ->
            page.results
                .filter { record -> isProperRecord(record) }
                .forEach { record -> database.ingest(recordToBeware(record)) }
        }
    return database
}

private fun renderToFile(database: Database, outputFilePath: String) {
    val engine = PebbleEngine.Builder().build()
    val compiledTemplate = engine.getTemplate("vlkv/report.html")
    val outputFile = File(outputFilePath)

    compiledTemplate.evaluate(
        outputFile.writer(), mapOf<String, Any>(
            "subjects" to database.getRecords(),
            "strip_url_desc_prefix" to "https://artistsbeware.info/beware",
        )
    )
}

private const val POSITIVE_REVIEW_REGEXP = "[aAbcDeFghiJlMmnNOoprsStuvy]{3,9} Positive Reviews 20\\d\\d"

private const val CATEGORY_ID_HIDDEN = 9

fun isProperRecord(record: Record): Boolean {
    if (Regex(POSITIVE_REVIEW_REGEXP).matches(record.title)) {
        return false
    }

    if (CATEGORY_ID_HIDDEN == record.category.id) {
        return false
    }

    return true
}
