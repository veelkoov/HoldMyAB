package vlkv.processing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import vlkv.fixes.Fixer
import vlkv.fixes.yaml.Fixes
import vlkv.tests.TestData
import vlkv.tests.TestsData
import kotlin.test.assertEquals

private val testsData = ObjectMapper(YAMLFactory())
    .registerModule(KotlinModule.Builder().build())
    .readValue(ClassLoader.getSystemResourceAsStream("testsData.yaml"), TestsData::class.java)

@Suppress("unused")
internal class NamesProcessorTest {
    private val processor = RecordProcessor(Fixer(Fixes.empty()))
    private val singleBadChar = Regex("[^ -~]", RegexOption.IGNORE_CASE)

    @TestFactory
    fun testGetNamesUrls(): List<DynamicTest> {
        return testsData.tests.map { testData: TestData ->
            dynamicTest("getNamesUrls: " + testData.input.replace(singleBadChar, "?")) {
                val result = processor.getNamesUrls(testData.input)

                val expected = listOf(testData.names.toSet(), testData.urls.toSet())
                val actual = listOf(result.names.toSet(), result.urls.toSet())

                assertEquals(expected, actual)
            }
        }
    }
}
