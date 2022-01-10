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

package com.ryanhtech.vocabulario.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.AdminPassActivity
import com.ryanhtech.vocabulario.admin.AdminPasswordManager
import com.ryanhtech.vocabulario.admin.AdminPermissions
import com.ryanhtech.vocabulario.utils.DataManager

open class VocabularioActivity : AppCompatActivity() {
    open val applyEmergencyBlock: Boolean = true
    open val isProtectedActivity: Boolean = false
    open val applyEndOfSupport: Boolean = true
    open val applyLicenseApprovalProtection = true

    /**
     * If set to true, this tells to the Android system that the
     * Activity must be secured (prevents screenshots and overlays).
     */
    open val isSecuredActivity: Boolean = false

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

        /*overridePendingTransition(R.anim.std_transition_in_frm_open,
            R.anim.std_transition_out_frm_open)*/
    }

    override fun onPause() {
        super.onPause()
        // Start the transition if the user is still in the app
        if (hasWindowFocus()) overridePendingTransition(R.anim.std_transition_in_frm_close,
                                    R.anim.std_transition_out_frm_close)
    }
}