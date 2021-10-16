package vlkv

import com.mitchellbosecke.pebble.PebbleEngine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import vlkv.fixes.Fixer
import vlkv.json.Record
import vlkv.json.RecordsPage
import vlkv.pebble.Extension
import vlkv.processing.getUnusedInvalidNames
import vlkv.processing.recordToBeware
import vlkv.processing.validateAssumptions
import java.io.File


fun main(args: Array<String>) {
    if (args.size != 2) {
        error("Require exactly two arguments: 1. path to the input directory, 2. path to the report file to generate")
    }

    val inputDirPath = args[0]
    val outputFilePath = args[1]

    val database = readInputFilesFromDir(inputDirPath)
    renderToFile(database, outputFilePath)

    println("Unused invalid names: " + getUnusedInvalidNames().joinToString(", "))
    println("Finished!")
}

private fun readInputFilesFromDir(inputDirPath: String): Database {
    val database = Database()

    val fixer = Fixer("$inputDirPath/fixes.yaml") // TODO: Possibly parametrize

    File(inputDirPath).walkTopDown()
        .filter { it.isFile && it.name.endsWith(".json") }
        .map { Json.decodeFromString<RecordsPage>(it.readText()) }
        .forEach { page ->
            page.results
                .stream()
                .filter { record -> isProperRecord(record) }
                .map { record -> validateAssumptions(record); fixer.fix(record) }
                .forEach { record -> database.ingest(recordToBeware(record)) }
        }

    fixer.assertAllDone()

    return database
}

private fun renderToFile(database: Database, outputFilePath: String) {
    val engine = PebbleEngine.Builder().extension(Extension()).build()
    val compiledTemplate = engine.getTemplate("vlkv/templates/report.html")
    val outputFile = File(outputFilePath)

    compiledTemplate.evaluate(
        outputFile.writer(), mapOf<String, Any>(
            "subjects" to database.getRecords(),
        )
    )
}

private const val POSITIVE_REVIEW_REGEXP = "[aAbcDeFghiJlMmnNOoprsStuvy]{3,9} Positive Reviews 20\\d\\d"

private const val CATEGORY_ID_HIDDEN = 9

fun isProperRecord(record: Record): Boolean {
    if (Regex(POSITIVE_REVIEW_REGEXP).matches(record.title)) {
        return false
    }

    if (CATEGORY_ID_HIDDEN == record.category.id) { // Should be already filtered out during fetch
        return false
    }

    return !record.hidden // Should be already filtered out during fetch
}
