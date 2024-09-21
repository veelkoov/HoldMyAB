package vlkv.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import vlkv.Paths
import java.io.File
import vlkv.configuration.yaml.Configuration as YamlConfiguration
import vlkv.configuration.yaml.DataFixes as YamlDataFixes

object Loader {
    fun dataFixesFromYaml(path: String): DataFixes {
        val result = ObjectMapper(YAMLFactory())
            .registerModule(KotlinModule.Builder().build())
            .readValue(File(path), YamlDataFixes::class.java)

        return DataFixes(result.fixes.map { fix ->
            with(fix) {
                Fix(`in`, change.map { change ->
                    with(change) {
                        Change(what, from, to)
                    }
                })
            }
        })
    }

    fun configurationFromYaml(path: String): Configuration {
        val result = ObjectMapper(YAMLFactory())
            .registerModule(KotlinModule.Builder().build())
            .readValue(File(path), YamlConfiguration::class.java)

        with(result) {
            return Configuration(
                ignoredNamesCi,
                ignoredWhereLines,
                removedTextGeneral,
                ignoredTags,
                nonNameTags,
                titlePrefixStripRegexCi,
            )
        }
    }

    fun loadDefaultConfiguration() = configurationFromYaml(Paths.generalFixesPath)
}
