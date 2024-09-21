package vlkv

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File

object Paths {
    private val dataDirectory = File("data")
    private val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.toString()
    private val previousDate: String

    init {
        if (!dataDirectory.isDirectory) {
            throw FileSystemException(dataDirectory, reason = "Is not a directory")
        }

        previousDate = dataDirectory
            .listFiles { parent, childName -> childName != currentDate && File(parent, childName).isDirectory }
            ?.maxOf { it.name }
            ?: throw IllegalStateException("No previous state directory exists. Please generate an empty previous input.")
    }

    val dataFixesPath = "fixes_data.yaml"
    val generalFixesPath = "configuration.yaml"

    private val base: String = "data/$currentDate"
    val newInputDirPath = "${base}/input"
    val outputDatabaseFilePath = "${base}/database.txt"
    val outputHtmlFilePath = "${base}/output.html"
    val outputTxtFilePath = "${base}/output.txt"

    val oldInputDirPath = "data/$previousDate/input"
}
