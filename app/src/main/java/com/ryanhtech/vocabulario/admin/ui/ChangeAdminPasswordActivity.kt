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

package com.ryanhtech.vocabulario.admin.ui

import android.os.Bundle
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.setup.fragment.EnterAdminPasswordSetupFragment
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import kotlinx.android.synthetic.main.activity_change_admin_password.*

class ChangeAdminPasswordActivity : VocabularioActivity() {
    private val changePwdFragment = EnterAdminPasswordSetupFragment()
    override val applyEmergencyBlock: Boolean = false

    companion object {
        const val UNLOCK_APP_AFTER: String = "canGoBack"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_admin_password)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.changePasswordFragment, changePwdFragment)
            commit()
        }

        changePasswordButton.setOnClickListener {
            if (changePwdFragment.onNextPressed()) {
                finish()

                if (intent.getBooleanExtra(UNLOCK_APP_AFTER, false)) {
                    AdminPasswordManager.saveEmergencyBlockModeStatus(false, applicationContext)
                }
            }
        }
    }
}