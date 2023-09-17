package vlkv

import vlkv.configuration.Loader
import vlkv.fixes.Fixer
import vlkv.input.readRecordsFrom
import vlkv.output.dumpDatabaseToFile
import vlkv.output.renderHtmlToFile
import vlkv.output.renderTxtToFile
import vlkv.processing.processors.RecordProcessor

fun main() {
    val configuration = Loader.configurationFromYaml(Paths.generalFixesPath)
    val dataFixes = Loader.dataFixesFromYaml(Paths.dataFixesPath)

    val records = readRecordsFrom(Paths.inputDirPath)

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

    dumpDatabaseToFile(database, Paths.outputDatabaseFilePath)
    renderHtmlToFile(database, Paths.outputHtmlFilePath)
    renderTxtToFile(database, Paths.outputTxtFilePath)

    println("Finished!")
}
