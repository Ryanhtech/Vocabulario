/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ryanhtech.vocabulario.internal.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ryanhtech.vocabulario.R

object NotificationManager {
    /**
     * Channel ID for admin messages.
     */
    const val ADMIN_CHANNEL_ID: String = "admin"

    /**
     * Channel ID for admin alerts.
     */
    const val ADMIN_ALERTS_CHANNEL_ID: String = "adminAlerts"

    /**
     * Initialize notification channels. In Android 7.1 and lower,
     * this function has no effect.
     */
    fun initializeNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            return
        }

        // Administration channel
        val adminChannel = returnChannelInstance(
            context.getString(R.string.admin_notification_channel_title),
            context.getString(R.string.admin_notification_channel_description),
            NotificationManager.IMPORTANCE_LOW,
            ADMIN_CHANNEL_ID
        )

        // Admin alerts channel
        val adminAlertsChannel = returnChannelInstance(
            context.getString(R.string.admin_alerts_notification_channel_title),
            context.getString(R.string.admin_notification_channel_description),
            NotificationManager.IMPORTANCE_HIGH,
            ADMIN_ALERTS_CHANNEL_ID
        )

        // Register the channels
        val systemNotificationManager: NotificationManager
            = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        systemNotificationManager.createNotificationChannel(adminChannel!!)  // Always != null
        systemNotificationManager.createNotificationChannel(adminAlertsChannel!!)
    }

    private fun returnChannelInstance(name: String, channelDescription: String, importance: Int,
                id: String): NotificationChannel? {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            return null
        }

        return NotificationChannel(id, name, importance).apply {
            description = channelDescription
        }
    }

    fun setNotifChannelId(notif: NotificationCompat.Builder, channelId: String)
        : NotificationCompat.Builder
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notif.setChannelId(channelId)
        }

        return notif
    }

    fun dismissNonRuntimeNotifications(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            cancel(Notifications.ADMIN_UNLOCKED_NOTIF)
        }
    }

    fun dismissNotif(notif: Int, context: Context) {
        with(NotificationManagerCompat.from(context)) {
            cancel(notif)
        }
    }
}