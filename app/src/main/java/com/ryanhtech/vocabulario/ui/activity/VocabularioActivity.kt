/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.admin.internal.AdminPermissions
import com.ryanhtech.vocabulario.admin.ui.AdminPassActivity
import com.ryanhtech.vocabulario.internal.reset.LocalConfigurationRequest
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
}