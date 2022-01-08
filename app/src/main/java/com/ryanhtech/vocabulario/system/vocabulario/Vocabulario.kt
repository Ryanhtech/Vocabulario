/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ryanhtech.vocabulario.system.vocabulario

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.ryanhtech.vocabulario.admin.AdminPasswordManager
import com.ryanhtech.vocabulario.admin.deviceadmin.VocabularioDeviceAdminReceiver
import com.ryanhtech.vocabulario.system.crashhandler.CrashHandlerActivity
import com.ryanhtech.vocabulario.system.ryanhtech.appsupport.RyanhtechAppSupportCore
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