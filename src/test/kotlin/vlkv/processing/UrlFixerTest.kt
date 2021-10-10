@file:Suppress("unused")

package vlkv.processing

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import vlkv.fixUrls
import kotlin.test.assertEquals

internal class UrlFixerTest {
    @TestFactory
    fun testFixUrls() = listOf(
        "http://www.furaffinity.net/user/abcdefghij" to "https://furaffinity.net/user/abcdefghij/",
        "http://www.facebook.com/AbcdefGhijklmnop" to "https://www.facebook.com/AbcdefGhijklmnop",
    ).map { (input, expected) ->
        dynamicTest("fixUrl $input -> $expected") {
            assertEquals(expected, fixUrls(input))
        }
    }
}
