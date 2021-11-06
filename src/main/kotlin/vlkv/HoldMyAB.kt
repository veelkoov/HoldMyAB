package vlkv

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import vlkv.fixes.Fixer
import vlkv.fixes.yaml.Fixes
import vlkv.json.Record
import vlkv.json.RecordsPage
import vlkv.pebble.Extension
import vlkv.processing.RecordProcessor
import java.io.File

fun main(args: Array<String>) {
    if (args.size != 3) {
        error("Require exactly two arguments: 1. path to the fixes.yaml configuration, 2. path to the input directory, 3. path to the report file to generate")
    }

    val fixerConfigPath = args[0]
    val inputDirPath = args[1]
    val outputFilePath = args[2]

    val fixer = Fixer(Fixes.loadFromYaml(fixerConfigPath))
    val database = readInputFilesFromDir(inputDirPath, fixer)
    renderToFile(database, outputFilePath)

    println("Unused ignored names: " + fixer.ignoredNames.getUnusedList())
    println("Unused ignored where lines: " + fixer.ignoredWhereLines.getUnusedList())
    println("Unused ignored tags: " + fixer.ignoredTags.getUnusedList())
    println("Finished!")
}

private fun readInputFilesFromDir(inputDirPath: String, fixer: Fixer): Database {
    val database = Database()
    val processor = RecordProcessor(fixer)

    File(inputDirPath).walkTopDown()
        .filter { it.isFile && it.name.endsWith(".json") }
        .map { Json.decodeFromString<RecordsPage>(it.readText()) }
        .forEach { page ->
            page.results
                .stream()
                .filter { record -> isProperRecord(record) }
                .map { record -> processor.validate(record); fixer.fix(record) }
                .forEach { record -> database.ingest(processor.getBeware(record)) }
        }

    fixer.assertAllDone()

    return database
}

private fun renderToFile(database: Database, outputFilePath: String) {
    val engine = PebbleEngine.Builder().extension(Extension()).loader(ClasspathLoader()).build()
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
