package vlkv.fixes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import vlkv.fixes.yaml.Fix
import vlkv.fixes.yaml.Fixes
import vlkv.json.Record
import java.io.File

class Fixer(fixesFilePath: String) {
    private val fixes: Fixes

    init {
        fixes = ObjectMapper(YAMLFactory())
            .registerModule(KotlinModule())
            .readValue(File(fixesFilePath), Fixes::class.java)
    }

    fun fix(record: Record): Record {
        fixes.fixes.forEach { fix: Fix -> fix.apply(record) }

        return record
    }

    fun assertAllDone() {
        fixes.fixes.forEach { it.assertDone() }
    }
}
