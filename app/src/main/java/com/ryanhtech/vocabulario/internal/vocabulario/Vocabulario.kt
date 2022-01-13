/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.vocabulario

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.admin.deviceadmin.VocabularioDeviceAdminReceiver
import com.ryanhtech.vocabulario.internal.crashhandler.CrashHandlerActivity
import com.ryanhtech.vocabulario.internal.ryanhtech.appsupport.RyanhtechAppSupportCore
import java.io.IOException

class Vocabulario : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context

        fun getContext(): Context = mContext

        private lateinit var mGsonInstance: Gson

        fun getGson(): Gson {
            return try {
                mGsonInstance
            } catch (e: UninitializedPropertyAccessException) {
                mGsonInstance = Gson()
                mGsonInstance  // <return mGsonInstance>
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

        if (checkIfAppCrashModeEnabled()) {
            startActivity(
                Intent(
                    this,
                    CrashHandlerActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        AdminPasswordManager.checkIfAppBlockedAndNotify(getContext())
        VocabularioDeviceAdminReceiver.checkAdminDisabledWithoutPermission(mContext)

        try {
            VocabularioDeviceAdminReceiver.changeAdminEnabledStatus(false, mContext)
        } catch (Ignored: Exception) { }

        val supportCore = RyanhtechAppSupportCore(mContext)
        try {
            supportCore.installCore()
        } catch (exception: IOException) { }
    }

    private fun checkIfAppCrashModeEnabled(): Boolean {
        return FirebaseCrashlytics.getInstance().didCrashOnPreviousExecution()
    }
}