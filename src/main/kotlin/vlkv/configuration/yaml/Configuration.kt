package vlkv.configuration.yaml

import com.fasterxml.jackson.annotation.JsonProperty

data class Configuration(
    @JsonProperty("ignored_names_ci")
    val ignoredNamesCi: List<String>,

    @JsonProperty("ignored_where_ci")
    val ignoredWhereCi: List<String>,

    @JsonProperty("ignored_where_lines")
    val ignoredWhereLines: List<String>,

    @JsonProperty("removed_text_general")
    val removedTextGeneral: List<String>,

    @JsonProperty("ignored_tags")
    val ignoredTags: List<String>,

    @JsonProperty("non_name_tags")
    val nonNameTags: List<String>,
)
