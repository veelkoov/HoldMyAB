@file:Suppress("unused")

package vlkv.processing

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

private data class GNUI(
    val input: String,
    val expectedNames: Set<String>,
    val expectedUrls: Set<String>,
)

internal class NamesProcessorTest {
    @TestFactory
    fun getNamesUrls() = setOf(
        GNUI("@/AbCdEfG", setOf("@AbCdEfG"), setOf()),
        GNUI("@\\AbcdefgH", setOf("@AbcdefgH"), setOf()),

        GNUI("Abcdefghij on DA, FA", setOf(),
            setOf("https://furaffinity.net/user/Abcdefghij/", "https://www.deviantart.com/Abcdefghij")),

        GNUI("Abcdefghij@FA", setOf(), setOf("https://furaffinity.net/user/Abcdefghij/")),

        GNUI("DA https://www.deviantart.com/laseros FA https://www.furaffinity.net/user/laseros/",
            setOf(), setOf("https://www.deviantart.com/laseros", "https://furaffinity.net/user/laseros/")),

        GNUI("abcdefghi on FA, AbcdefgHijkl on DA",
            setOf(), setOf("https://www.deviantart.com/AbcdefgHijkl", "https://furaffinity.net/user/abcdefghi/")),

        GNUI("Mnopqr of FA / Abcdefhijk on DA",
            setOf(), setOf("https://www.deviantart.com/Abcdefhijk", "https://furaffinity.net/user/Mnopqr/")),

        GNUI("Abcd Efghi on facebook", setOf("Abcd Efghi on facebook"), setOf()),
    ).map { (input, expectedNames, expectedUrls) ->
        dynamicTest("getNamesUrls") {
            val result = getNamesUrls(input)
            assertEquals(expectedNames, result.names.toSet())
            assertEquals(expectedUrls, result.urls.toSet())
        }
    }
}
