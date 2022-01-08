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
import com.ryanhtech.vocabulario.setup.AppSetupFragment
import com.ryanhtech.vocabulario.setup.UserSetupActivity
import com.ryanhtech.vocabulario.setup.UserSetupList

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