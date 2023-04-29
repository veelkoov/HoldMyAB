package vlkv.configuration.yaml

import com.fasterxml.jackson.annotation.JsonProperty

class Configuration {
    @JsonProperty("ignored_names")
    lateinit var ignoredNames: List<String>

    @JsonProperty("ignored_where")
    lateinit var ignoredWhere: List<String>

    @JsonProperty("ignored_where_lines")
    lateinit var ignoredWhereLines: List<String>

    @JsonProperty("removed_text_general")
    lateinit var removedTextGeneral: List<String>

    @JsonProperty("ignored_tags")
    lateinit var ignoredTags: List<String>

    @JsonProperty("non_name_tags")
    lateinit var nonNameTags: List<String>
}
