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
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import kotlinx.android.synthetic.main.fragment_reset_app.*

class ResetAppFragment : AppSetupFragment() {
    override val nextStep: Int = UserSetupList.SETUP_RESET_APP_OPR

    private lateinit var globalView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        globalView = inflater.inflate(R.layout.fragment_reset_app, container, false)

        return globalView
    }

    override fun onNextPressed(): Boolean {
        if (resetAppChkbx1.isChecked
                && resetAppChkbx2.isChecked
                && resetAppChkbx3.isChecked) {
            return true
        }

        resetAppStatus.text = getString(R.string.check_all_checkboxes)
        return false
    }
}