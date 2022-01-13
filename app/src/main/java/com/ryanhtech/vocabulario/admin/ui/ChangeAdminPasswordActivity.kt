/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
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