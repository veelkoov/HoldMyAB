package vlkv

import vlkv.configuration.Loader
import vlkv.fixes.Fixer
import vlkv.input.readRecordsFrom
import vlkv.output.dumpDatabaseToFile
import vlkv.output.renderHtmlToFile
import vlkv.output.renderTxtToFile
import vlkv.processing.processors.RecordProcessor

private const val dataFixesPath = "fixes-data.yaml"
private const val generalFixesPath = "configuration.yaml"
private const val inputDirPath = "input"
private const val outputDatabaseFilePath = "output/database.txt"
private const val outputHtmlFilePath = "output/output.html"
private const val outputTxtFilePath = "output/output.txt"

fun main() {
    val configuration = Loader.configurationFromYaml(generalFixesPath)
    val dataFixes = Loader.dataFixesFromYaml(dataFixesPath)

    val records = readRecordsFrom(inputDirPath)

    val fixer = Fixer(dataFixes, configuration)
    val fixedRecords = fixer.fix(records)
    fixer.assertAllDone()

    val processor = RecordProcessor(configuration)
    val bewares = processor.getBewares(fixedRecords)

    val database = Database(bewares)


    println("Unused ignored names: " + configuration.ignoredNames.getUnusedList())
    println("Unused ignored where: " + configuration.ignoredWhere.getUnusedList())
    println("Unused ignored where lines: " + configuration.ignoredWhereLines.getUnusedList())
    println("Unused removed text general: " + configuration.removedTextGeneral.getUnusedList())
    println("Unused ignored tags: " + configuration.ignoredTags.getUnusedList())
    println("Unused ignored non-name tags: " + configuration.nonNameTags.getUnusedList())

    dumpDatabaseToFile(database, outputDatabaseFilePath)
    renderHtmlToFile(database, outputHtmlFilePath)
    renderTxtToFile(database, outputTxtFilePath)

    println("Finished!")
}
