/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.block

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.admin.ui.ChangeAdminPasswordActivity
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.utils.UiUtils
import com.ryanhtech.vocabulario.utils.Utils
import kotlinx.android.synthetic.main.activity_unlock_app_security_code.*

class UnlockAppSecurityCodeActivity : VocabularioActivity() {
    override val applyEmergencyBlock = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock_app_security_code)

        backButtonUnlock.setOnClickListener {
            finish()
        }

        unlockSecCodeDeviceId.text =
            getString(R.string.device_id) + Utils.getDeviceId(this)

        enterSecurityCodeEditText.addTextChangedListener(object: TextWatcher {
            // Don't do anything with these two
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wrongCodeInfoTextView.isVisible = false

                if (s!!.count() == 6) {
                    checkCode(s.toString())
                }
            }
        })
    }

    private fun checkCode(code: String) {
        if (!AdminPasswordManager.isSecurityCodeCorrect(applicationContext, code)) {
            incorrectCode()
            return
        }

        // If the code is OK
        startActivity(
            Intent(
                this,
                ChangeAdminPasswordActivity::class.java
            ).putExtra(ChangeAdminPasswordActivity.UNLOCK_APP_AFTER, true)
        )

        finish()
    }

    private fun incorrectCode() {
        enterSecurityCodeEditText.setText("")

        //UiUtils.animateViewIncorrectValue(unlockAppSecurityCodeTitle)
        UiUtils.animateViewIncorrectValue(enterSecurityCodeEditText)

        wrongCodeInfoTextView.isVisible = true
    }

    override fun onResume() {
        super.onResume()

        if (!AdminPasswordManager.isEmergencyModeEnabled(applicationContext)) {
            finish()
        }
    }
}