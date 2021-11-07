package vlkv.output

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import vlkv.Database
import vlkv.output.pebble.Extension
import java.io.File

fun renderToFile(database: Database, outputFilePath: String) {
    val template = getEngine().getTemplate("vlkv/templates/report.html")
    val outputFile = File(outputFilePath)

    template.evaluate(
        outputFile.writer(), mapOf<String, Any>(
            "subjects" to database.getSortedRecords(),
        )
    )
}

private fun getEngine() = PebbleEngine.Builder()
    .extension(Extension())
    .loader(ClasspathLoader())
    .build()
