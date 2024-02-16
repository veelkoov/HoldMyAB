package vlkv

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Paths {
    private val base: String = "data/" + Clock.System.now().toLocalDateTime(TimeZone.UTC).date.toString()

    val dataFixesPath = "fixes_data.yaml"
    val generalFixesPath = "configuration.yaml"

    val oldInputDirPath = "data/2023-12-17/input" // TODO: Automate
    val newInputDirPath = "${base}/input"
    val outputDatabaseFilePath = "${base}/database.txt"
    val outputHtmlFilePath = "${base}/output.html"
    val outputTxtFilePath = "${base}/output.txt"
}
