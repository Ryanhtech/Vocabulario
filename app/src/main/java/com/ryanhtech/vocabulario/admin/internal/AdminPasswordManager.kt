/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.admin.internal

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.ryanhtech.vocabulario.internal.block.EmergencyBlockActivity
import com.ryanhtech.vocabulario.internal.notifications.NotificationManager
import com.ryanhtech.vocabulario.internal.notifications.Notifications
import com.ryanhtech.vocabulario.internal.services.AlertVibratorService
import com.ryanhtech.vocabulario.security.Hash
import com.ryanhtech.vocabulario.setup.config.UserSetupStatus
import com.ryanhtech.vocabulario.utils.Utils
import java.io.File
import java.io.FileNotFoundException

class AdminPasswordManager {
    /**
     * Manages the administrator password.
     */

    companion object {
        const val PASSWORD_FILE = "pwd.ini"
        const val SECURITY_CODE_FILE = "secCode.ini"
        const val EMERG_BLOCK_FILE = "blk.ini"

        fun setPassword(applicationContext: Context, password: String) {
            File(applicationContext.filesDir, PASSWORD_FILE)
                .writeText(Hash.getHash(password, "SHA-512"))
        }

        fun isPasswordCorrect(applicationContext: Context, password: String): Boolean {
            return File(
                applicationContext.filesDir,
                PASSWORD_FILE
            ).readText() == Hash.getHash(password, "SHA-512")
        }

        fun setSecurityCode(applicationContext: Context): String {
            if (UserSetupStatus.adminPassword == "") {
                throw IllegalStateException("Password cannot be \"\"!")
            }

            val securityCode = (100000..999999).random().toString()

            File(applicationContext.filesDir, SECURITY_CODE_FILE)
                .writeText(Hash.getHash(securityCode))

            return securityCode
        }

        fun isSecurityCodeCorrect(applicationContext: Context, code: String): Boolean {
            return File(applicationContext.filesDir, SECURITY_CODE_FILE)
                .readText() == Hash.getHash(code)
        }

        fun startEmergencyBlockMode(applicationContext: Context) {
            // Remember that the Emergency Block mode is enabled

            saveEmergencyBlockModeStatus(true, applicationContext)

            // Vibrate the device to alert people

            applicationContext.startService(
                Intent(applicationContext, AlertVibratorService::class.java)
            )

            // Start the block activity

            applicationContext.startActivity(
                Intent(
                    applicationContext,
                    EmergencyBlockActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        fun saveEmergencyBlockModeStatus(status: Boolean, applicationContext: Context) {
            if (status) {
                File(applicationContext.filesDir, EMERG_BLOCK_FILE)
                    .createNewFile()

                return
            }

            try {
                File(applicationContext.filesDir, EMERG_BLOCK_FILE)
                    .delete()
            } catch (e: FileNotFoundException) {}

            checkIfAppBlockedAndNotify(applicationContext)
        }

        fun isEmergencyModeEnabled(applicationContext: Context): Boolean {
            return File(applicationContext.filesDir, EMERG_BLOCK_FILE).exists()
        }

        fun startAlertVibration(applicationContext: Context) {
            while (AlertVibratorService.vibTimes <= AlertVibratorService.vibTarget) {
                Thread.sleep(1000)

                Utils().vibratePhone(1000, applicationContext)

                Thread.sleep(1000)

                AlertVibratorService.vibTimes += 1
            }
        }

        fun checkIfEmergencyModeEnabledAndLock(context: AppCompatActivity,
                                               autoFinish: Boolean = true): Boolean {
            if (isEmergencyModeEnabled(context)) {
                context.startActivity(
                    Intent(
                        context,
                        EmergencyBlockActivity::class.java
                    )
                )

                if (autoFinish) context.finish()

                return true
            }

            return false
        }

        fun checkIfAppBlockedAndNotify(context: Context) {
            if (isEmergencyModeEnabled(context)) {
                Notifications.notifyAppBlocked(context)
            } else {
                NotificationManager.dismissNotif(Notifications.ADMIN_APP_BLOCKED_NOTIF, context)
            }
        }
    }
}