package vlkv.processing

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import vlkv.fixes.Fixer
import vlkv.fixes.yaml.Fixes
import kotlin.test.assertEquals

private data class GNUI(
    val input: String,
    val expectedNames: Set<String>,
    val expectedUrls: Set<String>,
)

private val DATA_SETS = listOf(
    GNUI("@/AbCdEfG", setOf("@AbCdEfG"), setOf()),
    GNUI("@\\AbcdefgH", setOf("@AbcdefgH"), setOf()),

    GNUI(
        "Abcdefghij on DA, FA",
        setOf(), setOf("https://furaffinity.net/user/Abcdefghij/", "https://www.deviantart.com/Abcdefghij")
    ),

    GNUI("Abcdefghij@FA", setOf(), setOf("https://furaffinity.net/user/Abcdefghij/")),

    GNUI(
        "DA https://www.deviantart.com/laseros FA https://www.furaffinity.net/user/laseros/",
        setOf(), setOf("https://www.deviantart.com/laseros", "https://furaffinity.net/user/laseros/")
    ),

    GNUI(
        "abcdefghi on FA, AbcdefgHijkl on DA",
        setOf(), setOf("https://www.deviantart.com/AbcdefgHijkl", "https://furaffinity.net/user/abcdefghi/")
    ),

    GNUI(
        "Mnopqr of FA / Abcdefhijk on DA",
        setOf(), setOf("https://www.deviantart.com/Abcdefhijk", "https://furaffinity.net/user/Mnopqr/")
    ),

    GNUI("Abcd Efghi on facebook", setOf("Abcd Efghi on facebook"), setOf()),

    GNUI(
        "Abcdefghijk -> www.abcdefghijk.deviantart.com, www.furaffinity.net/user/abcdefghijk",
        setOf("Abcdefghijk ->"),
        setOf("https://abcdefghijk.deviantart.com/", "https://furaffinity.net/user/abcdefghijk/")
    ),

    GNUI("AbcdefGhijklm on Furaffinity", setOf(), setOf("https://furaffinity.net/user/AbcdefGhijklm/")),
)

@Suppress("unused")
internal class NamesProcessorTest {
    private val processor = RecordProcessor(Fixer(Fixes.empty()))

    @TestFactory
    fun testGetNamesUrls(): List<DynamicTest> {
        return DATA_SETS.map { (input, expectedNames, expectedUrls) ->
            dynamicTest("getNamesUrls") {
                val result = processor.getNamesUrls(input)

                assertEquals(expectedNames, result.names.toSet())
                assertEquals(expectedUrls, result.urls.toSet())
            }
        }
    }
}
