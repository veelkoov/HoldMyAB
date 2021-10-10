@file:Suppress("unused")

package vlkv.processing

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

private data class GTWI(
    val where: String,
    val urlsFromWho: List<String>,
    val expected: List<String>,
)

internal class WhereProcessorTest {
    @TestFactory
    fun testGetTidyWhere() = listOf(
        GTWI("Abcdefghij on DA, FA", listOf(), listOf("https://furaffinity.net/user/Abcdefghij/", "https://www.deviantart.com/Abcdefghij")),
        GTWI("Abcdefghij@FA", listOf(), listOf("https://furaffinity.net/user/Abcdefghij/")),
    ).map { (where, urlsFromWho, expected) ->
        dynamicTest("getTidyWhere") {
            assertEquals(expected, getTidyWhere(where, urlsFromWho))
        }
    }
}
