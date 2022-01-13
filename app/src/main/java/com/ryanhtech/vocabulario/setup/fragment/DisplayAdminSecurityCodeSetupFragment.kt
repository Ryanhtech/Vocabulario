/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.setup.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.utils.Utils
import kotlinx.android.synthetic.main.fragment_display_admin_security_code_setup.view.*

class DisplayAdminSecurityCodeSetupFragment : AppSetupFragment() {

    private lateinit var globalView: View
    override val nextStep: Int = UserSetupList.ORG_ENABLE_ADMIN

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment

        globalView = inflater.inflate(R.layout.fragment_display_admin_security_code_setup, container, false)

        globalView.adminSecurityCode.text = generateCode()
        globalView.displaySecCodeDeviceId.text =
            getString(R.string.device_id) + Utils.getDeviceId(requireActivity())

        return globalView
    }

    private fun generateCode(): String {
        return AdminPasswordManager.setSecurityCode(requireActivity().applicationContext)
    }
}