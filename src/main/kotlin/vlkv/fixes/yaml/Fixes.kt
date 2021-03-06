package vlkv.fixes.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

class Fixes {
    lateinit var fixes: List<Fix>
    lateinit var ignoredNames: List<String>
    lateinit var ignoredWhere: List<String>
    lateinit var ignoredWhereLines: List<String>
    lateinit var removedTextGeneral: List<String>
    lateinit var ignoredTags: List<String>

    companion object {
        fun loadFromYaml(path: String): Fixes {
            return ObjectMapper(YAMLFactory())
                .registerModule(KotlinModule.Builder().build())
                .readValue(File(path), Fixes::class.java)
        }

        fun empty(): Fixes {
            val result = Fixes()

            result.fixes = listOf()
            result.ignoredNames = listOf()
            result.ignoredWhere = listOf()
            result.ignoredWhereLines = listOf()
            result.removedTextGeneral = listOf()
            result.ignoredTags = listOf()

            return result
        }
    }
}
