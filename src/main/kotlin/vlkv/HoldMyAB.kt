package vlkv

import vlkv.configuration.Loader
import vlkv.fixes.Fixer
import vlkv.input.readRecordsFrom
import vlkv.output.dumpDatabaseToFile
import vlkv.output.renderHtmlToFile
import vlkv.output.renderTxtToFile
import vlkv.processing.RecordProcessor

private const val dataFixesPath = "fixes-data.yaml"
private const val generalFixesPath = "configuration.yaml"
private const val inputDirPath = "input"
private const val outputDatabaseFilePath = "output/database.txt"
private const val outputHtmlFilePath = "output/output.html"
private const val outputTxtFilePath = "output/output.txt"

fun main() {
    val configuration = Loader.configurationFromYaml(generalFixesPath)
    val dataFixes = Loader.dataFixesFromYaml(dataFixesPath)
    val fixer = Fixer(dataFixes, configuration)

    val records = readRecordsFrom(inputDirPath)
    val fixedRecords = fixer.fix(records)

    val processor = RecordProcessor(configuration, fixer)
    val bewares = processor.getBewares(fixedRecords)

    val database = Database(bewares)

    fixer.assertAllDone()

    println("Unused ignored names: " + fixer.ignoredNames.getUnusedList())
    println("Unused ignored where lines: " + fixer.ignoredWhereLines.getUnusedList())
    println("Unused ignored tags: " + fixer.ignoredTags.getUnusedList())

    dumpDatabaseToFile(database, outputDatabaseFilePath)
    renderHtmlToFile(database, outputHtmlFilePath)
    renderTxtToFile(database, outputTxtFilePath)

    println("Finished!")
}
