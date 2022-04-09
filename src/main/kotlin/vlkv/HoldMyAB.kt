package vlkv

import vlkv.fixes.Fixer
import vlkv.fixes.yaml.Fixes
import vlkv.input.readRecordsFrom
import vlkv.output.renderToFile
import vlkv.processing.RecordProcessor

fun main(args: Array<String>) {
    if (args.size != 3) {
        error("Require exactly two arguments: 1. path to the fixes.yaml configuration, 2. path to the input directory, 3. path to the report file to generate")
    }

    val fixerConfigPath = args[0]
    val inputDirPath = args[1]
    val outputFilePath = args[2]

    val fixer = Fixer(Fixes.loadFromYaml(fixerConfigPath))
    val processor = RecordProcessor(fixer)

    val records = readRecordsFrom(inputDirPath)
    val fixedRecords = fixer.fix(records)
    val bewares = processor.getBewares(fixedRecords)
    val database = Database(bewares)

    fixer.assertAllDone()

    println("Unused ignored names: " + fixer.ignoredNames.getUnusedList())
    println("Unused ignored where lines: " + fixer.ignoredWhereLines.getUnusedList())
    println("Unused ignored tags: " + fixer.ignoredTags.getUnusedList())

    renderToFile(database, outputFilePath)

    println("Finished!")
}
