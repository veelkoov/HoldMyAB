@file:Suppress("unused")

package vlkv.json

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

data class GNI(
    val title: String,
    val who: String,
    val expected: List<String>,
)

internal class NamesProcessorTest {
    @TestFactory
    fun testGetNames() = listOf(
        GNI("@/AbCdEfG", "@AbCdEfG", listOf("@AbCdEfG")),
    ).map { (title, who, expected) ->
        dynamicTest("getNames") {
            assertEquals(expected, getNames(title, who))
        }
    }
}
