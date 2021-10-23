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

    @TestFactory
    fun testGetNamesUrls(): List<DynamicTest> {
        return testsData.tests.map { testData: TestData ->
            dynamicTest("getNamesUrls") {
                val result = processor.getNamesUrls(testData.input)

                assertEquals(testData.names.toSet(), result.names.toSet())
                assertEquals(testData.urls.toSet(), result.urls.toSet())
            }
        }
    }
}
