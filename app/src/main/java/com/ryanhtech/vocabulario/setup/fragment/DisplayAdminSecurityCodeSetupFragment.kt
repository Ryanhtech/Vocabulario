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

package com.ryanhtech.vocabulario.setup.fragment

import android.annotation.SuppressLint
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.utils.Utils
import kotlinx.android.synthetic.main.fragment_display_admin_security_code_setup.view.*

class DisplayAdminSecurityCodeSetupFragment : AppSetupFragment() {
    override val nextStep: Int = UserSetupList.ORG_ENABLE_ADMIN
    override val fragmentLayout: Int = R.layout.fragment_display_admin_security_code_setup
    override val fragmentIconResource = R.drawable.ic_baseline_password_24
    override val fragmentTitleResource = R.string.admin_security_code
    override val fragmentDescriptionResource = R.string.admin_security_code_descr


    private fun generateCode(): String {
        return AdminPasswordManager.setSecurityCode(requireActivity().applicationContext)
    }

    @SuppressLint("SetTextI18n")
    override fun startJob() {
        super.startJob()

        // Set the info on the screen
        globalView?.adminSecurityCode?.text = generateCode()
        globalView?.displaySecCodeDeviceId?.text =
            getString(R.string.device_id) + Utils.getDeviceId(requireActivity())
    }
}