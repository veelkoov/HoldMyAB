package vlkv.configuration.yaml

import com.fasterxml.jackson.annotation.JsonProperty

class Configuration {
    @JsonProperty("ignored_names_ci")
    lateinit var ignoredNamesCi: List<String>

    @JsonProperty("ignored_where_ci")
    lateinit var ignoredWhereCi: List<String>

    @JsonProperty("ignored_where_lines_ci")
    lateinit var ignoredWhereLinesCi: List<String>

    @JsonProperty("removed_text_general")
    lateinit var removedTextGeneral: List<String>

    @JsonProperty("ignored_tags")
    lateinit var ignoredTags: List<String>

    @JsonProperty("non_name_tags")
    lateinit var nonNameTags: List<String>
}
