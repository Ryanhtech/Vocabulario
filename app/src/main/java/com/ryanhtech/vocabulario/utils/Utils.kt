/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.utils

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Process
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.DatePicker
import com.ryanhtech.vocabulario.internal.receiver.BootStartupReceiver
import com.ryanhtech.vocabulario.ui.startup.SplashScreenActivity
import java.util.*

class Utils {
    // Miscellaneous utilities used for the correct working of Vocabulario

    fun checkIfActivityAnimationSupported(): Boolean {
        // Check if the activity animations are supported. They are supported since
        // Android Lollipop (5.0).

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    fun getStringFromOutsideActivity(resid: Int, applicationContext: Context): String {
        return applicationContext.getString(resid)
    }

    fun restartApp(applicationContext: Context) {
        /**
         * Restarts ¡Vocabulario!.
         */
        val intent = Intent(
                applicationContext,
                SplashScreenActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        applicationContext.startActivity(intent)
    }

    fun killApp() {
        Process.killProcess(Process.myPid())
    }

    fun checkIfDarkModeEnabled(applicationContext: Context): Boolean {

        return when (applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    fun setupReminderService(applicationContext: Context) {
        // Enable the receiver that will start the service after Android's boot
        val receiver = ComponentName(applicationContext, BootStartupReceiver::class.java)

        applicationContext.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    fun vibratePhone(milliseconds: Long, applicationContext: Context) {
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    milliseconds,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(milliseconds)
        }
    }

    companion object {
        fun isInternetConnected(applicationContext: Context): Boolean {
            val connMgr = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Get the network capabilities from the ConnectivityManager
                val netCapabilities = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
                    ?: return false

                return when {
                    netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)      -> true
                    netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)  -> true
                    netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)  -> true
                    netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else                                                                  -> false
                }
            } else {
                return connMgr.activeNetworkInfo?.isConnected
                    ?: return false
            }
        }

        fun isWiFiConnected(applicationContext: Context): Boolean {
            val connMgr = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Get the network capabilities from the ConnectivityManager
                val netCapabilities = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
                    ?: return false

                return when {
                    netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)      -> true
                    else                                                                  -> false
                }
            } else {
                return connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.isConnected
                    ?: return false
            }
        }

        fun displayAppInfo(context: Context, packageName: String) {
            val intent = Intent()

            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data   = Uri.fromParts("package", packageName, null)

            context.startActivity(intent)
        }

        fun getDateFromDatePicker(datePicker: DatePicker): Calendar {
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year

            return Calendar.getInstance().apply {
                set(year, month, day)
            }
        }

        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                .uppercase()
        }
    }
}