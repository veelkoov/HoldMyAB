@file:Suppress("unused")

package vlkv.processing

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

private data class GNUI(
    val input: String,
    val expectedNames: List<String>,
    val expectedUrls: List<String>,
)

internal class NamesProcessorTest {
    @TestFactory
    fun getNamesUrls() = listOf(
        GNUI("Abcdefghij on DA, FA", listOf(), listOf("https://furaffinity.net/user/Abcdefghij/", "https://www.deviantart.com/Abcdefghij")),
        GNUI("Abcdefghij@FA", listOf(), listOf("https://furaffinity.net/user/Abcdefghij/")),
        GNUI("@/AbCdEfG", listOf("@AbCdEfG"), listOf()),
    ).map { (input, expectedNames, expectedUrls) ->
        dynamicTest("getNamesUrls") {
            val result = getNamesUrls(input)
            assertEquals(expectedNames, result.names)
            assertEquals(expectedUrls, result.urls)
        }
    }
}
