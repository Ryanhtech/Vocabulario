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

package com.ryanhtech.vocabulario.internal.ryanhtech.familylink

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

/**
 * This class provides features to determine if the device on which we
 * are running is currently supervised using Google Family Link. If so,
 * disable the features that are not required for children to use this
 * app (e.g. admin supervision).
 *
 * This class uses advanced Family Link recognition technology, which
 * are using no Google API. It scans for the Family Link Helper app,
 * and for the Family Link profile owner. If both are activated, we can
 * say that the device is supervised.
 *
 * Google might not approve my solution, however it's to protect kids
 * that I coded this scanner. I can't use any other way to determine if
 * the device is supervised, so this is the only way I could use.
 */
class FamilyLinkScan(context: Context) {
    // Family Link "Helper" package name
    // It's the helper because it's useless, the only thing it does
    // is that it opens Google Play services to display parental control
    // information. This app actually sucks.
    private val familyLinkHelperAppPackage = "com.google.android.apps.kids.familylinkhelper"

    // This is the Google Play services package name
    // It is critical to the Family Link correct working, because it
    // contains the Family Link Manager extension which actually
    // manages the supervision and it also controls the profile owner.
    private val familyLinkManagerAppPackage = "com.google.android.gms"

    // This is the Family Link profile owner Android component name,
    // which is built-in into the Family Link Manager extension.
    // It helps scanning for Family Link, and this scanner won't work
    // without this.
    private val familyLinkProfileOwner = ComponentName(familyLinkManagerAppPackage,
        "$familyLinkManagerAppPackage.kids.account.receiver.ProfileOwnerReceiver")

    // The local context.
    // It is useful to preform system calls.
    private val mContext = context

    /**
     * This scans for the Family Link helper app.
     *
     * If the Family Link helper app was found, return true. Else,
     * return false. (private method)
     */
    private fun scanFamilyLinkHelperPackage(): Boolean {
        return try {
            mContext.packageManager.getPackageGids(familyLinkHelperAppPackage)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * This verifies if the Family Link Manager profile owner is
     * enabled. If so, return true, else false.
     *
     * It first verifies if the Google Play services package is
     * a profile owner, and then it checks if the Family Link
     * Manager component name is admin. If both are true, we can
     * say that the Family Link Manager profile owner is activated.
     *
     * CAUTION: This check could fail, if the Google Play services
     * package has registered a profile owner, and if the user enabled
     * the Family Link Manager profile owner in the Settings as admin
     * only. Meanwhile, no one will do such thing, it's completely
     * stupid, because kids are usually running away of Family Link
     * and don't want it on their phone. So you don't have much chance
     * that this check fails.
     */
    private fun scanFamilyLinkManagerEnabled(): Boolean {
        val dpm = mContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        return dpm.isAdminActive(familyLinkProfileOwner)
                && dpm.isProfileOwnerApp(familyLinkManagerAppPackage)
    }

    /**
     * Checks if the device is Google Family Linked. It uses advanced
     * Family Link recognition technology, and if you want to learn more,
     * check out the JavaDoc of this class.
     *
     * @return if Family Link is active on the profile.
     * @since initial version
     * @author Ryanhtech Labs
     */
    fun isDeviceFamilyLinked(): Boolean {
        return scanFamilyLinkHelperPackage() && scanFamilyLinkManagerEnabled()
    }
}