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
        "https://www.furaffinity.net/user/abcde.fghij, https://www.instagram.com/asdfghjkl/" to "https://furaffinity.net/user/abcde.fghij/ https://www.instagram.com/asdfghjkl/",
        "https://www.deviantart.com/abc1d2ef\r\nhttps://toyhou.se/asdfghjk\r\nhttps://m.facebook.com/abcd.efgh.ijkl\r\nhttps://instagram.com/abcdefghijklm?utm_medium=copy_link" to "https://www.deviantart.com/abc1d2ef\r\nhttps://toyhou.se/asdfghjk\r\nhttps://www.facebook.com/abcd.efgh.ijkl\r\nhttps://www.instagram.com/abcdefghijklm/",
        "https://instagram.com/abcd_efgh\r\nhttps://toyhou.se/abcde_fghi" to "https://www.instagram.com/abcd_efgh/\r\nhttps://toyhou.se/abcde_fghi",
    ).map { (input, expected) ->
        dynamicTest("fixUrl $input -> $expected") {
            assertEquals(expected, Urls.tidy(input))
        }
    }
}
