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
import android.widget.Toast
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.setup.config.UserSetupStatus
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_setup_organization_enter_name.view.*

class EnterOrganizationNameSetupFragment : AppSetupFragment() {

    private lateinit var globalView: View
    override val nextStep: Int = UserSetupList.ORG_SETUP_ENTER_PWD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        globalView = inflater.inflate(R.layout.fragment_setup_organization_enter_name, container, false)

        return globalView
    }

    override fun onNextPressed(): Boolean {
        if (globalView.setupOrgName.text.length < 4) {
            alertUserIncorrectName()

            return false
        }
        UserSetupStatus.orgName = globalView.setupOrgName.text.toString()
        UserSetupList.isOrgPasswordConfigPending = true

        return true
    }

    private fun alertUserIncorrectName() {
        Toast.makeText(activity, R.string.enter_real_name, Toast.LENGTH_LONG)
            .show()

        UiUtils.animateEditTextIncorrect(globalView.setupOrgName)
    }
}