package vlkv.fixes

object StringFixer {
    fun fix(input: String): String {
        var result = input

        result = result.replace(" ", " ") // NBSP
        result = result.replace("\r\n", "\n")

        return result
    }
}
