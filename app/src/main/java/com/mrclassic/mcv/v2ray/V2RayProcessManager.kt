package com.mrclassic.mcv.v2ray

import android.content.Context
import timber.log.Timber
import java.io.File

class V2RayProcessManager(private val context: Context) {
    private var process: Process? = null

    fun start(configPath: String): Boolean {
        val bin = resolveBinary() ?: return false.also { Timber.w("V2Ray binary not found") }
        return try {
            val pb = ProcessBuilder(bin.absolutePath, "-config", configPath)
                .redirectErrorStream(true)
            pb.environment()["XRAY_LOCATION_ASSET"] = bin.parentFile?.absolutePath ?: ""
            process = pb.start()
            Timber.i("V2Ray started: ${bin.absolutePath}")
            true
        } catch (t: Throwable) {
            Timber.e(t, "Failed to start V2Ray")
            false
        }
    }

    fun stop() {
        runCatching { process?.destroy() }.onFailure { Timber.e(it) }
        process = null
    }

    private fun resolveBinary(): File? {
        // Prefer binary placed in app's internal files dir
        val internal = File(context.filesDir, "v2ray/v2ray")
        if (internal.canExecute()) return internal
        // Fallback to bundled jniLibs path (if copied)
        val abiList = listOf("arm64-v8a", "armeabi-v7a", "x86_64", "x86")
        val base = File(context.applicationInfo.nativeLibraryDir ?: return null)
        abiList.forEach { _ ->
            val candidate = File(base, "libv2ray.so")
            if (candidate.exists()) return candidate
        }
        // Last resort: asset copied path not found
        return null
    }
}