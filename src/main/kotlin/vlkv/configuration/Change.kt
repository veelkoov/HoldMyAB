package vlkv.configuration

data class Change(
    val what: String,
    val from: String,
    val to: String,
) {
    private var done: Boolean = false

    fun isDone() = done

    fun markAsDone() {
        done = true
    }
}
