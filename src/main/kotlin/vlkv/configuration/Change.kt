package vlkv.configuration

data class Change(
    val what: String,
    val from: String,
    val to: String,
) {
    var done: Boolean = false
}
