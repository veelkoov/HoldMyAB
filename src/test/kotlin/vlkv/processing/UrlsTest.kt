@file:Suppress("unused", "HttpUrlsUsage")

package vlkv.processing

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

internal class UrlsTest {
    @TestFactory
    fun testTidy() = listOf(
        "http://www.furaffinity.net/user/abcdefghij" to "https://furaffinity.net/user/abcdefghij/",
        "http://www.facebook.com/AbcdefGhijklmnop" to "https://www.facebook.com/AbcdefGhijklmnop",
        "Old Fa Account: http://www.furaffinity.net/user/abcdefghijk0011\r\nNew Fa Account: http://www.furaffinity.net/user/abcdefg\r\nDeviantArt Account: http://abcdefg.deviantart.com/" to "https://furaffinity.net/user/abcdefghijk0011/\r\nhttps://furaffinity.net/user/abcdefg/\r\nhttps://abcdefg.deviantart.com/",
        "www.furaffinity.com/user/abcdefghijklm" to "https://furaffinity.net/user/abcdefghijklm/",
    ).map { (input, expected) ->
        dynamicTest("fixUrl $input -> $expected") {
            assertEquals(expected, Urls.tidy(input))
        }
    }
}
