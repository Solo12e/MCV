package com.mrclassic.mcv.ssh

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class SshTunnelManager {
    private var session: Session? = null

    suspend fun connect(host: String, port: Int, username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val jsch = JSch()
            val s = jsch.getSession(username, host, port)
            s.setPassword(password)
            val config = java.util.Properties().apply {
                put("StrictHostKeyChecking", "no")
            }
            s.setConfig(config)
            s.connect(10_000)
            session = s
            Timber.i("SSH connected to $host:$port")
            true
        }.getOrElse {
            Timber.e(it, "SSH connection failed")
            false
        }
    }

    suspend fun disconnect() = withContext(Dispatchers.IO) {
        runCatching { session?.disconnect() }.onFailure { Timber.e(it) }
        session = null
    }
}