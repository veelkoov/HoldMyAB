package vlkv.fixes.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

class DataFixes {
    lateinit var fixes: List<Fix>

    companion object {
        fun loadFromYaml(path: String): DataFixes {
            return ObjectMapper(YAMLFactory())
                .registerModule(KotlinModule.Builder().build())
                .readValue(File(path), DataFixes::class.java)
        }

        fun empty(): DataFixes {
            val result = DataFixes()

            result.fixes = listOf()

            return result
        }
    }
}
