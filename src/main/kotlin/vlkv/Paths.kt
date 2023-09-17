package vlkv

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Paths {
    private val base: String = "data/" + Clock.System.now().toLocalDateTime(TimeZone.UTC).date.toString()

    val dataFixesPath = "fixes-data.yaml"
    val generalFixesPath = "configuration.yaml"

    val inputDirPath = "${base}/input"
    val outputDatabaseFilePath = "${base}/database.txt"
    val outputHtmlFilePath = "${base}/output.html"
    val outputTxtFilePath = "${base}/output.txt"
}
