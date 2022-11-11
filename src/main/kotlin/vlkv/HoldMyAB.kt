package vlkv

import vlkv.fixes.Fixer
import vlkv.fixes.yaml.DataFixes
import vlkv.fixes.yaml.GeneralFixes
import vlkv.input.readRecordsFrom
import vlkv.output.renderHtmlToFile
import vlkv.output.renderTxtToFile
import vlkv.processing.RecordProcessor

private const val dataFixesPath = "fixes-data.yaml"
private const val generalFixesPath = "fixes-general.yaml"
private const val inputDirPath = "input"
private const val outputHtmlFilePath = "output/output.html"
private const val outputTxtFilePath = "output/output.txt"

fun main() {
    val fixer = Fixer(DataFixes.loadFromYaml(dataFixesPath), GeneralFixes.loadFromYaml(generalFixesPath))
    val processor = RecordProcessor(fixer)

    val records = readRecordsFrom(inputDirPath)
    val fixedRecords = fixer.fix(records)
    val bewares = processor.getBewares(fixedRecords)
    val database = Database(bewares)

    fixer.assertAllDone()

    println("Unused ignored names: " + fixer.ignoredNames.getUnusedList())
    println("Unused ignored where lines: " + fixer.ignoredWhereLines.getUnusedList())
    println("Unused ignored tags: " + fixer.ignoredTags.getUnusedList())

    renderHtmlToFile(database, outputHtmlFilePath)
    renderTxtToFile(database, outputTxtFilePath)

    println("Finished!")
}
