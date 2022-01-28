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

package com.ryanhtech.vocabulario.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.admin.internal.AdminPermissions
import com.ryanhtech.vocabulario.admin.ui.AdminPassActivity
import com.ryanhtech.vocabulario.internal.reset.LocalConfigurationRequest
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.ui.popup.PopupContainerActivity
import com.ryanhtech.vocabulario.ui.startup.SplashScreenActivity
import com.ryanhtech.vocabulario.utils.DataManager

/**
 * This provides the base Activity class for all Vocabulario
 * activities. It helps defining custom properties for Activities
 * in Vocabulario.
 *
 * @since initial version
 * @author Ryanhtech Labs
 */
open class VocabularioActivity : AppCompatActivity() {
    /**
     * If you should allow this Activity starting even if
     * the app is in Forgot password mode, set this to `false`.
     */
    open val applyEmergencyBlock: Boolean = true

    /**
     * If it's better to ask an administrator password to start
     * this app, set this to `true`.
     */
    open val isProtectedActivity: Boolean = false

    /**
     * If this Activity must start even if the app's maintenance is
     * discontinued, set this to `false`.
     */
    open val applyEndOfSupport: Boolean = true

    /**
     * If you should not prevent this Activity from starting if the user
     * hasn't accepted the software license, set this to `false`.
     */
    open val applyLicenseApprovalProtection = true

    /**
     * If set to `true`, this tells to the Android system that the
     * Activity must be secured (prevents screenshots and overlays).
     */
    open val isSecuredActivity: Boolean = false

    /**
     * If set to `true`, the Activity will close if the app isn't configured
     * properly after a local reset.
     */
    open val applyLocalResetConfigurationRequest = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                // Finish the Activity
                finish()
            }
        }

        super.onCreate(savedInstanceState)

        if (applyEmergencyBlock) {
            AdminPasswordManager.checkIfEmergencyModeEnabledAndLock(this)
        }

        if (isProtectedActivity && !AdminPermissions.adminUnlocked
            && DataManager().isManagedByOrganization(this)
            && DataManager.checkIfAppConfigured(this)) {

            registerResult.launch(
                Intent(
                    this,
                    AdminPassActivity::class.java
                )
            )
        }

        if (isSecuredActivity) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    override fun onResume() {
        super.onResume()

        if (applyLocalResetConfigurationRequest
            && LocalConfigurationRequest.isReConfigRequested(this)) {
            // Start the splash screen to re-configure the app
            startActivity(Intent(this, SplashScreenActivity::class.java))
            finish()
        }
    }

    open fun displayPopupFragment(fragmentInst: Fragment, activityContext: Activity) {
        // Put the root view into a local variable
        val lActivityRootView = activityContext.window.decorView.rootView

        // Convert them to JSON
        val lGsonInst = Vocabulario.getGson()
        val lActivityRootViewJson = lGsonInst.toJson(lActivityRootView)
        val lFragmentJson = lGsonInst.toJson(fragmentInst)

        // Now initialize the Intent and put the string extras into it
        val lPopupIntent = Intent(activityContext, PopupContainerActivity::class.java)

        // Set the new task flag else it won't work
        lPopupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // Set the extras
        lPopupIntent.apply {
            putExtra(PopupContainerActivity.EXTRA_FRAGMENT_TO_SET, lFragmentJson)
            putExtra(PopupContainerActivity.EXTRA_PARENT_ACTIVITY_ROOTVIEW, lActivityRootViewJson)
        }

        // Start the popup Activity using our Intent
        try {
            activityContext.startActivity(lPopupIntent)
        }
        catch (err: Exception) {
            // An exception occurred while starting the Activity
            Log.e("VocabularioActivity",
                "An error occurred while starting the PopupContainerActivity.")
            throw err
        }
    }
}