package vlkv.fixes

object StringFixer {
    val multispace = Regex(" +")

    fun fix(input: String): String {
        var result = input

        result = result.replace(" ", " ") // NBSP
        result = result.replace("\r\n", "\n")
        result = result.replace(multispace, " ")

        return result
    }
}
