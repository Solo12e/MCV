package com.mrclassic.mcv.core.vpn

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.mrclassic.mcv.MCVApp
import com.mrclassic.mcv.R
import com.mrclassic.mcv.ui.MainActivity
import timber.log.Timber

class MCVVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(MCVApp.NOTIFICATION_ID_VPN, buildNotification())
        setupVpn()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        vpnInterface?.close()
        vpnInterface = null
    }

    private fun setupVpn() {
        try {
            val builder = Builder()
                .setSession("MCV")
                .addAddress("10.0.0.2", 32)
                .addDnsServer("1.1.1.1")
                .addRoute("0.0.0.0", 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                builder.setMetered(false)
            }
            vpnInterface = builder.establish()
            Timber.i("VPN interface established")
        } catch (t: Throwable) {
            Timber.e(t, "Failed to establish VPN")
            stopSelf()
        }
    }

    private fun buildNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, MCVApp.NOTIFICATION_CHANNEL_VPN)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("VPN is running")
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()
    }
}