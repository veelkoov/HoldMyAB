package vlkv.output

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import vlkv.BewareSubject
import vlkv.Database
import vlkv.output.pebble.Extension
import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime

fun dumpDatabaseToFile(database: Database, outputFilePath: String) {
    assureParentDirectoryExists(outputFilePath)

    File(outputFilePath).printWriter().use { file ->
        var first = true

        database.getSortedRecords().forEach { subject: BewareSubject ->
            if (first) {
                first = false
            } else {
                file.println("\n---")
            }

            file.println(subject)

            subject.getBewaresSorted().forEach(file::println)
        }
    }
}

fun renderHtmlToFile(database: Database, outputFilePath: String) {
    assureParentDirectoryExists(outputFilePath)

    val template = getEngine().getTemplate("vlkv/templates/report.html")
    val outputFile = File(outputFilePath)

    template.evaluate(
        outputFile.writer(), mapOf<String, Any>(
            "subjects" to database.getSortedRecords(),
            "last_update" to ZonedDateTime.now(ZoneId.of("UTC")),
        )
    )
}

fun renderTxtToFile(database: Database, outputFilePath: String) {
    assureParentDirectoryExists(outputFilePath)

    File(outputFilePath).printWriter().use { file ->
        database.getSortedRecords().forEach { subject ->
            file.println("=== SUBJECT ===")

            subject.getTagsSorted().forEach { file.println("<$it>") }
            subject.getNamesSorted().forEach { file.println(it) }

            file.println("=== URLS ===")

            subject.getUrlsSorted().forEach { file.println(it) }

            file.println("=== B/C ===")

            subject.getBewaresSorted().forEach { beware ->
                beware.tags.filter { it.contains("archive") }.forEach { file.print("<$it> ") }

                file.println("${beware.getCaption()} ${beware.abUrl}")

                if (beware.resolved) {
                    file.println(" - RESOLVED")
                }
            }

            file.println("")
        }
    }
}

private fun getEngine() = PebbleEngine.Builder()
    .extension(Extension())
    .loader(ClasspathLoader())
    .strictVariables(true) // What a DUMB default and what a STUPID setting and what an HOPELESS "programmer" (me)
    .build()

private fun assureParentDirectoryExists(outputFilePath: String) {
    File(outputFilePath).parentFile.mkdirs()
}
