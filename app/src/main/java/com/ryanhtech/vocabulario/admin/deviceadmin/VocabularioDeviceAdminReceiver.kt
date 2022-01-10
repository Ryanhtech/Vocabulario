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

package com.ryanhtech.vocabulario.admin.deviceadmin

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPermissions
import com.ryanhtech.vocabulario.internal.notifications.NotificationManager
import com.ryanhtech.vocabulario.internal.notifications.Notifications
import com.ryanhtech.vocabulario.utils.DataManager
import kotlinx.coroutines.runBlocking

class VocabularioDeviceAdminReceiver : DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)

        NotificationManager.dismissNotif(Notifications.ADMIN_DISABLED_NOTIF, context)

        if (!DataManager().isManagedByOrganization(context)
                && DataManager.checkIfAppConfigured(context)) {
            getDpm(context).removeActiveAdmin(getComponentName())
        }
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        return context.getString(R.string.disable_admin_warning)
    }

    override fun onDisabled(context: Context, intent: Intent): Unit = runBlocking {
        Thread {
            // Wait 1 second for the admin to be disabled
            Thread.sleep(1000)

            checkAdminDisabledWithoutPermission(context)
        }.start()
    }

    companion object {
        fun getComponentName(): ComponentName {
            return ComponentName(
                "com.ryanhtech.vocabulario",
                "com.ryanhtech.vocabulario.admin.deviceadmin.VocabularioDeviceAdminReceiver"
            )
        }

        fun checkAdminDisabledWithoutPermission(context: Context) {
            if (!AdminPermissions.adminUnlocked && DataManager().isManagedByOrganization(context)
                && !getDpm(context).isAdminActive(getComponentName())) {
                Notifications.notifyAdminDisabled(context)
            }
        }

        fun requestEnableAdmin(context: Context) {
            context.startActivity(
                Intent(context,
                    EnableAdminProxyActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }

        fun changeAdminEnabledStatus(isEnabled: Boolean, context: Context) {
            if (getDpm(context).isAdminActive(this.getComponentName())) return

            val enabledStatus = if (isEnabled) PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                else PackageManager.COMPONENT_ENABLED_STATE_DISABLED

            context.packageManager.setComponentEnabledSetting(
                this.getComponentName(),
                enabledStatus,
                PackageManager.DONT_KILL_APP
            )
        }

        fun getDpm(context: Context): DevicePolicyManager {
            return context.getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE)
                    as DevicePolicyManager
        }
    }
}