@file:Suppress("HttpUrlsUsage")

package vlkv.processing

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

private val DATA_SETS = listOf(
    "http://www.furaffinity.net/user/abcdefghij" to "https://furaffinity.net/user/abcdefghij/",
    "www.furaffinity.com/user/abcdefghijklm" to "https://furaffinity.net/user/abcdefghijklm/",

    "http://www.facebook.com/AbcdefGhijklmnop" to "https://www.facebook.com/AbcdefGhijklmnop",

    "Old Fa Account: http://www.furaffinity.net/user/abcdefghijk0011\r\nNew Fa Account: http://www.furaffinity.net/user/abcdefg\r\nDeviantArt Account: http://abcdefg.deviantart.com/"
            to "https://furaffinity.net/user/abcdefghijk0011/\r\nhttps://furaffinity.net/user/abcdefg/\r\nhttps://abcdefg.deviantart.com/",

    "https://www.furaffinity.net/user/abcde.fghij, https://www.instagram.com/asdfghjkl/"
            to "https://furaffinity.net/user/abcde.fghij/ https://www.instagram.com/asdfghjkl/",

    "https://www.deviantart.com/abc1d2ef\r\nhttps://toyhou.se/asdfghjk\r\nhttps://m.facebook.com/abcd.efgh.ijkl\r\nhttps://instagram.com/abcdefghijklm?utm_medium=copy_link"
            to "https://www.deviantart.com/abc1d2ef\r\nhttps://toyhou.se/asdfghjk\r\nhttps://www.facebook.com/abcd.efgh.ijkl\r\nhttps://www.instagram.com/abcdefghijklm/",

    "https://instagram.com/abcd_efgh\r\nhttps://toyhou.se/abcde_fghi"
            to "https://www.instagram.com/abcd_efgh/\r\nhttps://toyhou.se/abcde_fghi",

    "Twitter: https://twitter.com/AbcdEfGhijk\r\nFuraffinity: https://www.furaffinity.net/user/abcdefghij\r\nPicarto: https://picarto.tv/abcdefghijart\r\nTwitter: https://twitter.com/AbcdeFghijkl"
            to "https://twitter.com/AbcdEfGhijk\r\nhttps://furaffinity.net/user/abcdefghij/\r\nhttps://picarto.tv/abcdefghijart\r\nhttps://twitter.com/AbcdeFghijkl",

    "eBay: https://www.ebay.com/usr/abcdefs\nTwitter: http://twitter.com/Abcdef\nTumblr: http://abcdefart.tumblr.com\nand http://abcdefs.tumblr.com\nFacebook: http://facebook.com/abcdef\nInkBunny: http://inkbunny.net/abcdef"
            to "https://www.ebay.com/usr/abcdefs\nhttps://twitter.com/Abcdef\nhttps://abcdefart.tumblr.com/\nand https://abcdefs.tumblr.com/\nhttps://www.facebook.com/abcdef\nhttps://inkbunny.net/abcdef",

    "FurAffinity: http://www.furaffinity.net/user/abcdefghi/\nPatreon: https://www.patreon.com/Abcdefghi\nTumblr: http://abcdefghi-artwork.tumblr.com/\nTwitter: https://twitter.com/AbcdEfGh\nDeviantArt: https://www.deviantart.com/abcdefghi\nWeasyl: https://www.weasyl.com/profile/abcdefghi\nTrello: https://trello.com/b/aa1BbCCd/abcdefghi-commissions-queue"
            to "https://furaffinity.net/user/abcdefghi/\nhttps://www.patreon.com/Abcdefghi\nhttps://abcdefghi-artwork.tumblr.com/\nhttps://twitter.com/AbcdEfGh\nhttps://www.deviantart.com/abcdefghi\nhttps://www.weasyl.com/profile/abcdefghi\nhttps://trello.com/b/aa1BbCCd/abcdefghi-commissions-queue",

    "https://www.patreon.com/AbcdEfghi,\nFur Affinity: http://www.furaffinity.net/user/abcd01/\nDeviant art: https://abcd-efghi.deviantart.com/"
            to "https://www.patreon.com/AbcdEfghi,\nhttps://furaffinity.net/user/abcd01/\nhttps://abcd-efghi.deviantart.com/",

    "https://twitter.com/Abcd12345678?t=1aaaAAAaaAA11aaaaA_1aa&s=09" to "https://twitter.com/Abcd12345678",
)

@Suppress("unused")
internal class UrlsTest {
    @TestFactory
    fun testTidy(): List<DynamicTest> {
        return DATA_SETS.map { (input, expected) ->
            dynamicTest("fixUrl $input -> $expected") {
                assertEquals(expected, Urls.tidy(input))
            }
        }
    }
}
