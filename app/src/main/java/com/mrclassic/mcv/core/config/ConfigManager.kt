package com.mrclassic.mcv.core.config

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.mrclassic.mcv.security.KeyStoreHelper
import java.io.File
import java.util.UUID

@Serializable
data class McvConfig(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: String,
    val data: String
)

class ConfigManager(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
    private val dir: File = File(context.filesDir, "configs").apply { mkdirs() }

    fun listConfigs(): List<McvConfig> =
        dir.listFiles { f -> f.extension == "mcvc" }?.mapNotNull { file ->
            runCatching { load(file) }.getOrNull()
        } ?: emptyList()

    fun save(config: McvConfig) {
        val plain = json.encodeToString(McvConfig.serializer(), config).encodeToByteArray()
        val encrypted = KeyStoreHelper.encrypt(plain)
        File(dir, "${config.id}.mcvc").writeBytes(encrypted)
    }

    fun delete(configId: String) {
        File(dir, "$configId.mcvc").delete()
    }

    private fun load(file: File): McvConfig {
        val decrypted = KeyStoreHelper.decrypt(file.readBytes())
        return json.decodeFromString(McvConfig.serializer(), decrypted.decodeToString())
    }
}