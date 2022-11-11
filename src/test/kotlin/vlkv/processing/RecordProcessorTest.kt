package vlkv.processing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import vlkv.fixes.Fixer
import vlkv.fixes.yaml.DataFixes
import vlkv.fixes.yaml.GeneralFixes
import vlkv.tests.RecordProcessorTestData
import vlkv.tests.RecordProcessorTestsData
import kotlin.test.assertEquals

private val testsData = ObjectMapper(YAMLFactory())
    .registerModule(KotlinModule.Builder().build())
    .readValue(ClassLoader.getSystemResourceAsStream("RecordProcessorTestData.yaml"), RecordProcessorTestsData::class.java)

@Suppress("unused")
internal class RecordProcessorTest {
    private val processor = RecordProcessor(Fixer(DataFixes.empty(), GeneralFixes.empty()))
    private val singleBadChar = Regex("[^ -~]", RegexOption.IGNORE_CASE)

    @TestFactory
    fun testGetNamesUrls(): List<DynamicTest> {
        return testsData.tests.map { testData: RecordProcessorTestData ->
            val testName = "getNamesUrls: " + testData.input.replace(singleBadChar, "?")

            dynamicTest(testName) {
                doTestGetNamesUrls(testData)
            }
        }
    }

    private fun doTestGetNamesUrls(testData: RecordProcessorTestData) {
        val result = processor.getNamesUrls(testData.input)

        val expected = listOf(testData.names.toSet(), testData.urls.toSet())
        val actual = listOf(result.names.toSet(), result.urls.toSet())

        assertEquals(expected, actual)
    }
}
