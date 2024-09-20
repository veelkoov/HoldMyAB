package vlkv

import vlkv.configuration.Configuration
import vlkv.configuration.Loader
import vlkv.fixes.AutoFixer
import vlkv.fixes.ManualFixer
import vlkv.input.readRecordsFrom
import vlkv.output.dumpDatabaseToFile
import vlkv.output.renderHtmlToFile
import vlkv.output.renderTxtToFile
import vlkv.processing.processors.RecordProcessor

fun main() {
    val configuration = Loader.loadDefaultConfiguration()
    val manualFixer = createManualFixer(configuration)
    val autoFixer = AutoFixer(configuration)
    val processor = RecordProcessor(configuration)

    println("Generating report for ${Paths.newInputDirPath}, compared to ${Paths.oldInputDirPath}")

    val newDatabase = createDatabase(Paths.newInputDirPath, manualFixer, autoFixer, processor)

    manualFixer.assertAllDone()

    val oldDatabase = createDatabase(Paths.oldInputDirPath, manualFixer, autoFixer, processor)

    printDifferences(oldDatabase, newDatabase)

    println("Unused ignored names: " + configuration.ignoredNames.getUnusedList())
    println("Unused ignored where: " + configuration.ignoredWhere.getUnusedList())
    println("Unused ignored where lines: " + configuration.ignoredWhereLines.getUnusedList())
    println("Unused removed text general: " + configuration.removedTextGeneral.getUnusedList())
    println("Unused ignored tags: " + configuration.ignoredTags.getUnusedList())
    println("Unused ignored non-name tags: " + configuration.nonNameTags.getUnusedList())

    dumpDatabaseToFile(newDatabase, Paths.outputDatabaseFilePath)
    renderHtmlToFile(newDatabase, Paths.outputHtmlFilePath)
    renderTxtToFile(newDatabase, Paths.outputTxtFilePath)

    println("Finished!")
}

private fun createManualFixer(configuration: Configuration): ManualFixer {
    val dataFixes = Loader.dataFixesFromYaml(Paths.dataFixesPath)

    return ManualFixer(configuration, dataFixes)
}

private fun createDatabase(
    inputDirectoryPath: String,
    manualFixer: ManualFixer,
    autoFixer: AutoFixer,
    processor: RecordProcessor,
): Database {
    var records = readRecordsFrom(inputDirectoryPath)

    records = manualFixer.fix(records)
    records = autoFixer.fix(records)

    val bewares = processor.getBewares(records)
    val database = Database(bewares)

    return database
}

private fun printDifferences(oldDatabase: Database, newDatabase: Database) {
    val oldResult = oldDatabase.getSortedRecords().associateBy { it.getLowestBewareId() }
    val newResult = newDatabase.getSortedRecords().associateBy { it.getLowestBewareId() }

    oldResult.filterNot { newResult.containsKey(it.key) }.forEach { (_, oldSubject) ->
        println("VANISHED: $oldSubject")
    }

    newResult.forEach { (lowestBewareId, newSubject) ->
        val oldSubject = oldResult[lowestBewareId]

        if (null == oldSubject) {
            println("ADDED ${newSubject.getNamesSorted().joinToString(" / ")}")

            newSubject.getBewaresSorted().forEach {
                print(compareBewares(it, null))
            }

            println()
        } else {
            var description = ""

            val newAliases = newSubject.getNamesSorted().minus(oldSubject.getNamesSorted())
            if (newAliases.isNotEmpty()) {
                description += "ADDED ALIAS(ES) ${newAliases.joinToString(" / ")}\n"
            }

            newSubject.getBewaresSorted().forEach { newBeware ->
                val oldBeware = oldSubject.getBewaresSorted().singleOrNull { it.id == newBeware.id }

                description += compareBewares(newBeware, oldBeware)
            }

            if ("" != description) {
                println(oldSubject.getNamesSorted().joinToString(" / "))
                print(description)
                println()
            }
        }
    }
}

fun compareBewares(new: Beware, old: Beware?): String {
    if (old != null
        && new.resolved == old.resolved
        && new.isBeware == old.isBeware
        && new.isArchive == old.isArchive
        && new.abUrl == old.abUrl
    ) {
        return ""
    }

    var result = ""

    if (null == old) {
        result += "ADDED "
    }

    if (new.resolved && (old == null || !old.resolved)) {
        result += "RESOLVED "
    }

    if (null != old && new.isBeware != old.isBeware) {
        result += "${old.getCaption()} ---> "
    }

    result += "${new.getCaption()} "

    if (old == null) {
        if (new.isArchive) {
            result += "(archive) "
        }
    } else {
        result += if (new.isArchive == old.isArchive) {
            "(archive) "
        } else if (new.isArchive) {
            "(NOW ARCHIVE) "
        } else {
            "(NO LONGER ARCHIVE) "
        }
    }

    result += new.abUrl + "\n"

    if (old != null && new.abUrl != old.abUrl) {
        result += "    URL changed from: ${old.abUrl}\n"
    }

    return result
}
