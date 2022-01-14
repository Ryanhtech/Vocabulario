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

import android.widget.Toast
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.setup.config.UserSetupStatus
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_setup_organization_enter_name.view.*

class EnterOrganizationNameSetupFragment : AppSetupFragment() {
    override val nextStep: Int = UserSetupList.ORG_SETUP_ENTER_PWD

    override val fragmentLayout: Int = R.layout.fragment_setup_organization_enter_name
    override val fragmentIconResource: Int = R.drawable.ic_baseline_business_24
    override val fragmentTitleResource: Int = R.string.org_name

    override fun onNextPressed(): Boolean {
        if (globalView!!.setupOrgName.text.length < 4) {
            alertUserIncorrectName()

            return false
        }
        UserSetupStatus.orgName = globalView!!.setupOrgName.text.toString()
        UserSetupList.isOrgPasswordConfigPending = true

        return true
    }

    private fun alertUserIncorrectName() {
        Toast.makeText(activity, R.string.enter_real_name, Toast.LENGTH_LONG)
            .show()

        UiUtils.animateEditTextIncorrect(globalView!!.setupOrgName)
    }
}