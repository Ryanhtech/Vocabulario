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

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.deviceadmin.EnableAdminProxyActivity
import com.ryanhtech.vocabulario.ui.startup.SplashScreenActivity

class Notifications {
    companion object {
        /**
         * Notification for admin unlock.
         */
        const val ADMIN_UNLOCKED_NOTIF: Int = 0

        /**
         * Notification for the vibrator.
         */
        const val VIBRATOR_ALERT_NOTIF: Int = 1

        /**
         * Notification for unauthorized admin shut down.
         */
        const val ADMIN_DISABLED_NOTIF: Int = -1

        /**
         * Notification for blocked app.
         */
        const val ADMIN_APP_BLOCKED_NOTIF: Int = -2

        fun notifyAdminUnlocked(context: Context) {
            val intent = Intent(context, SplashScreenActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent,
                getDefaultPendingIntentFlag())

            var builder = NotificationCompat.Builder(context, NotificationManager.ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_lock_open_24)
                .setContentTitle(context.getString(R.string.admin_unlocked_notif_title))
                .setContentText(context.getString(R.string.admin_unlocked_notif))
                .setStyle(NotificationCompat.BigTextStyle().bigText(
                    context.getString(R.string.admin_unlocked_notif)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)

            builder =
                NotificationManager.setNotifChannelId(builder, NotificationManager.ADMIN_CHANNEL_ID)

            with(NotificationManagerCompat.from(context)) {
                notify(ADMIN_UNLOCKED_NOTIF, builder.build())
            }
        }

        fun notifyVibratorAlert(context: Context) {
            var builder = NotificationCompat.Builder(context, NotificationManager.ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_vibration_24)
                .setContentTitle(context.getString(R.string.vibrator_notif))
                .setContentText(context.getString(R.string.vibrator_notif_text))
                .setStyle(NotificationCompat.BigTextStyle().bigText(
                    context.getString(R.string.vibrator_notif_text)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)

            builder =
                NotificationManager.setNotifChannelId(builder, NotificationManager.ADMIN_CHANNEL_ID)

            with(NotificationManagerCompat.from(context)) {
                notify(VIBRATOR_ALERT_NOTIF, builder.build())
            }
        }

        fun notifyAdminDisabled(context: Context) {
            val intent = Intent(context, EnableAdminProxyActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent,
                getDefaultPendingIntentFlag())

            var builder = NotificationCompat.Builder(context, NotificationManager.ADMIN_ALERTS_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_error_24)
                .setContentTitle(context.getString(R.string.unauthorized_admin_disabled))
                .setContentText(context.getString(R.string.click_notif_to_resolve))
                .setStyle(NotificationCompat.BigTextStyle().bigText(
                    context.getString(R.string.click_notif_to_resolve)))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setContentIntent(pendingIntent)

            builder =
                NotificationManager.setNotifChannelId(builder, NotificationManager.ADMIN_ALERTS_CHANNEL_ID)

            with(NotificationManagerCompat.from(context)) {
                notify(ADMIN_DISABLED_NOTIF, builder.build())
            }
        }

        fun notifyAppBlocked(context: Context) {
            val intent = Intent(context, SplashScreenActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent,
                getDefaultPendingIntentFlag())

            var builder = NotificationCompat.Builder(context, NotificationManager.ADMIN_ALERTS_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_lock_24)
                .setContentTitle(context.getString(R.string.app_blocked))
                .setContentText(context.getString(R.string.click_notif_to_resolve))
                .setStyle(NotificationCompat.BigTextStyle().bigText(
                    context.getString(R.string.click_notif_to_resolve)))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setContentIntent(pendingIntent)

            builder =
                NotificationManager.setNotifChannelId(builder, NotificationManager.ADMIN_ALERTS_CHANNEL_ID)

            with(NotificationManagerCompat.from(context)) {
                notify(ADMIN_APP_BLOCKED_NOTIF, builder.build())
            }
        }

        fun getDefaultPendingIntentFlag(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                0
            }
        }
    }
}