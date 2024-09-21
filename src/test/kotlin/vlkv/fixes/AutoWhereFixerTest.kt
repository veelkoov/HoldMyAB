package vlkv.fixes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import vlkv.configuration.Loader

class AutoWhereFixerTest {
    private val testsSet = mapOf(
        "Approached via Notes on Furaffinity" to "",
        "Through FA, then through Discord DMs" to ", ", // FIXME, good enough for mow
        "AbcdeFghi on Furaffinity" to "https://furaffinity.net/user/AbcdeFghi/",
        "Discord / ToyHouse / Furaffinity" to "//", // FIXME, good enough for mow
        "On Twitter and Tumblr" to "",
    )

    private val subject = AutoWhereFixer(Loader.loadDefaultConfiguration())

    @TestFactory
    fun testFixingWhere(): List<DynamicTest> = testsSet.map { (input, expected) ->
        DynamicTest.dynamicTest(input) {
            assertEquals(expected, subject.fix(input))
        }
    }
}
