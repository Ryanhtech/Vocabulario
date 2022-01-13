/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.setup.fragment

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.deviceadmin.VocabularioDeviceAdminReceiver
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.base.UserSetupActivity
import com.ryanhtech.vocabulario.setup.config.UserSetupList

class EnableDeviceAdminSetupFragment : AppSetupFragment() {
    private lateinit var globalView: View
    override val nextStep: Int = UserSetupList.SETUP_FEATURES_PERS

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment

        globalView = inflater.inflate(R.layout.fragment_enable_admin_setup, container, false)

        return globalView
    }

    override fun onNextPressed(): Boolean {
        super.onNextPressed()

        VocabularioDeviceAdminReceiver.requestEnableAdmin(requireActivity())
        return false
    }

    override fun onResume() {
        super.onResume()

        val dpm = requireActivity().getSystemService(Context.DEVICE_POLICY_SERVICE)
                as DevicePolicyManager

        if (dpm.isAdminActive(VocabularioDeviceAdminReceiver.getComponentName())) {
            requireActivity().startActivity(
                Intent(requireActivity(),
                    UserSetupActivity::class.java).putExtra("step",
                    UserSetupList.SETUP_FEATURES_PERS
                )
            )

            requireActivity().finish()
        }
    }
}