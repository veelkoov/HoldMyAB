package vlkv.output

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import vlkv.Database
import vlkv.output.pebble.Extension
import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime

fun renderHtmlToFile(database: Database, outputFilePath: String) {
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
    File(outputFilePath).printWriter().use { file ->
        database.getSortedRecords().forEach { subject ->
            file.println("=== SUBJECT ===")

            subject.getTagsSorted().forEach { file.println("<$it>") }
            subject.getNamesSorted().forEach { file.println(it) }

            file.println("=== URLS ===")

            subject.getUrlsSorted().forEach { file.println(it) }

            file.println("=== B/C ===")

            subject.getBewaresSorted().forEach { beware ->
                beware.allTags.filter { it.contains("archive") }.forEach { file.print("<$it> ") }

                file.print(if (beware.isBeware) "Beware " else "Caution ")
                file.println(beware.url)

                if (beware.resolved) {
                    file.println(" - RESOLVED")
                }
            }
        }
    }
}

private fun getEngine() = PebbleEngine.Builder()
    .extension(Extension())
    .loader(ClasspathLoader())
    .build()
