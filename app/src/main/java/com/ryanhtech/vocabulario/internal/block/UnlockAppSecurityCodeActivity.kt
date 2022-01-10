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