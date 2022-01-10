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

package com.ryanhtech.vocabulario.internal.ryanhtech.familylink

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

class FamilyLinkScan(context: Context) {
    private val familyLinkHelperAppPackage = "com.google.android.apps.kids.familylinkhelper"
    private val familyLinkManagerAppPackage = "com.google.android.gms"
    private val familyLinkProfileOwner = ComponentName(familyLinkManagerAppPackage,
        "$familyLinkManagerAppPackage.kids.account.receiver.ProfileOwnerReceiver")

    private val mContext = context

    private fun scanFamilyLinkHelperPackage(): Boolean {
        return try {
            mContext.packageManager.getPackageGids(familyLinkHelperAppPackage)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun scanFamilyLinkManagerEnabled(): Boolean {
        val dpm = mContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        return dpm.isAdminActive(familyLinkProfileOwner)
                && dpm.isProfileOwnerApp(familyLinkManagerAppPackage)
    }

    /**
     * Checks if the device is Google Family Linked.
     *
     * @return if Family Link is active on the internal.
     */
    fun isDeviceFamilyLinked(): Boolean {
        return scanFamilyLinkHelperPackage() && scanFamilyLinkManagerEnabled()
    }
}