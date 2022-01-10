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

package com.ryanhtech.vocabulario.admin

import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.deviceadmin.VocabularioDeviceAdminReceiver
import com.ryanhtech.vocabulario.system.block.ForgotPasswordBlockPromptActivity
import com.ryanhtech.vocabulario.system.notifications.Notifications
import com.ryanhtech.vocabulario.ui.activity.VocabularioPopupActivity
import com.ryanhtech.vocabulario.utils.DataManager
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.activity_admin_pass.*
import java.io.FileNotFoundException

class AdminPassActivity : VocabularioPopupActivity() {
    var tries: Int = 5

    private var goneToForgotPwd = false

    // Prevent screenshots
    override val isSecuredActivity = true

    companion object {
        const val EXTRA_IS_DISABLING_ADMIN = "isDisablingAdminExtra"
    }

    override val isProtectedActivity: Boolean = false

    private var isDisablingAdmin = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pass)

        try {
            orgManagedBy.text = "${getString(R.string.admin_permission_owner)}${
                DataManager().getOrganizationName(applicationContext)
            }"
        } catch (e: FileNotFoundException) {
            finish()
        }

        isDisablingAdmin = intent.getBooleanExtra(EXTRA_IS_DISABLING_ADMIN, false)

        adminPassPwdSetupConfirm.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!AdminPasswordManager.isPasswordCorrect(applicationContext, adminPassPwdSetupConfirm.text.toString())) {
                    adminPassProgress.isVisible   =   true
                    adminPassConnect.isEnabled    =   false
                    wrongPassword()
                    true
                }
                else {
                    bootAdminAction()
                    false
                }
            } else {
                false
            }
        }

        adminPassConnect.setOnClickListener {
        }

        adminPassPwdSetupConfirm.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                adminPassConnect.isEnabled = s.toString() != ""
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        adminPassForgotPwd.setOnClickListener {
            forgotPassword()
        }
    }

    override fun onBackPressed() {
        if (isDisablingAdmin) {
            unavailableFunction()
            return
        }

        setResult(RESULT_CANCELED)
        finish()
    }

    private fun bootAdminAction() {
        Notifications.notifyAdminUnlocked(this)

        AdminPermissions.adminUnlocked = true

        if (isDisablingAdmin) {
            val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
            dpm.removeActiveAdmin(VocabularioDeviceAdminReceiver.getComponentName())

            finish()
            return
        }

        // Finish the Activity with the OK result
        setResult(RESULT_OK)
        finish()
    }

    private fun wrongPassword() {
        adminPassConnect.isEnabled = true
        adminPassProgress.isVisible = false

        //UiUtils.animateViewIncorrectValue(adminPassIcon)
        UiUtils.animateViewIncorrectValue(textInputLayoutConfirm)

        adminPassPwdSetupConfirm.setText("")

        tries -= 1

        if (tries == 0) {

        }
    }

    private fun unavailableFunction() {
        Toast.makeText(
            this,
            getString(R.string.blocked_function_disable_admin), Toast.LENGTH_SHORT
        ).show()
    }

    private fun forgotPassword() {
        if (isDisablingAdmin) {
            unavailableFunction()
            return
        }

        goneToForgotPwd = true

        val outAnim = AnimationUtils.loadAnimation(
            this,
            R.anim.zoom_close_out_fast
        )

        Thread {
            runOnUiThread {
                adminPassConstraintLayout.startAnimation(outAnim)
            }

            Thread.sleep(outAnim.duration)

            runOnUiThread {
                adminPassConstraintLayout.isVisible = false
            }

            runOnUiThread {
                processForgotPassword()
            }
        }.start()
    }

    private fun processForgotPassword() {
        startActivity(
            Intent(
                this,
                ForgotPasswordBlockPromptActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        )
    }

    override fun onResume() {
        super.onResume()

        if (goneToForgotPwd) {
            Handler(Looper.getMainLooper()).postDelayed({
                adminPassConstraintLayout.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.zoom_in_splash
                    )
                )

                adminPassConstraintLayout.isVisible = true

                goneToForgotPwd = false
            }, 0)
        }
    }
}