package vlkv.configuration.yaml

data class Change(
    val what: String,
    val from: String,
    val to: String,
    @Suppress("unused") // It's in the YAML for clarity
    val why: String,
)
