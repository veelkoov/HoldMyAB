package vlkv.fixes.yaml

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

class GeneralFixes {
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

    companion object {
        fun loadFromYaml(path: String): GeneralFixes {
            return ObjectMapper(YAMLFactory())
                .registerModule(KotlinModule.Builder().build())
                .readValue(File(path), GeneralFixes::class.java)
        }

        fun empty(): GeneralFixes {
            val result = GeneralFixes()

            result.ignoredNames = listOf()
            result.ignoredWhere = listOf()
            result.ignoredWhereLines = listOf()
            result.removedTextGeneral = listOf()
            result.ignoredTags = listOf()

            return result
        }
    }
}
