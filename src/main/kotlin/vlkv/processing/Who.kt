package vlkv.processing

private val NON_INFORMATIVES = setOf(
    "artist",
    "buyer",
)

fun fixWho(input: String, issues: MutableList<String>): String {
    if (NON_INFORMATIVES.contains(input.lowercase())) {
        issues.add("Ignoring who: '$input'")

        return ""
    }

    return input
}
