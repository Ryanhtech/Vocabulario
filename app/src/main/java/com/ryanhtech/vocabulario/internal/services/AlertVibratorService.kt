/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.internal.notifications.Notifications

class AlertVibratorService : Service() {
    companion object {
        var vibTimes = 0
        const val vibTarget = 15
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the vibrator and stop the service once it's done
        Thread {
            Notifications.notifyVibratorAlert(this)
            AdminPasswordManager.startAlertVibration(applicationContext)

            with(NotificationManagerCompat.from(this)) {
                cancel(Notifications.VIBRATOR_ALERT_NOTIF)
            }

            stopSelf()
        }.start()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}