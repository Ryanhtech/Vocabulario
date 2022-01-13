/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.setup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.setup.config.UserSetupStatus
import kotlinx.android.synthetic.main.activity_change_admin_password.*
import kotlinx.android.synthetic.main.activity_user_setup.*
import kotlinx.android.synthetic.main.fragment_enter_admin_password_setup.*

class EnterAdminPasswordSetupFragment : AppSetupFragment() {

    private lateinit var globalView: View
    override val nextStep: Int = UserSetupList.ORG_SETUP_SEC_CODE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment

        globalView = inflater.inflate(R.layout.fragment_enter_admin_password_setup, container, false)

        return globalView
    }

    override fun onNextPressed(): Boolean {
        val containerView = requireActivity().fragmentContainerViewSetup
            ?: requireActivity().changePasswordFragment

        if (adminPwdSetupEnter.text.toString() != adminPwdSetupEnterConfirm.text.toString()) {
            Snackbar.make(
                containerView,
                R.string.pwd_n_match,
                Snackbar.LENGTH_SHORT
            ).show()

            return false
        }
        if (adminPwdSetupEnter.text.toString().length < 4) {
            Snackbar.make(
                containerView,
                R.string.password_short,
                Snackbar.LENGTH_SHORT
            )
                .show()

            return false
        }

        UserSetupStatus.adminPassword = adminPwdSetupEnter.text.toString()

        return true
    }
}