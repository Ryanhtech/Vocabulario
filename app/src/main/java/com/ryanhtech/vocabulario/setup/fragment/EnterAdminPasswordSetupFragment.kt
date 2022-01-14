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

import com.google.android.material.snackbar.Snackbar
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.setup.config.UserSetupStatus
import kotlinx.android.synthetic.main.activity_change_admin_password.*
import kotlinx.android.synthetic.main.activity_user_setup.*
import kotlinx.android.synthetic.main.fragment_enter_admin_password_setup.*

class EnterAdminPasswordSetupFragment : AppSetupFragment() {
    override val nextStep: Int = UserSetupList.ORG_SETUP_SEC_CODE

    override val fragmentLayout: Int = R.layout.fragment_enter_admin_password_setup
    override val fragmentIconResource = R.drawable.ic_baseline_lock_24
    override val fragmentTitleResource = R.string.choose_admin_pwd
    override val fragmentDescriptionResource: Int = R.string.pwd_descr

    override fun onNextPressed(): Boolean {
        val containerView = requireActivity().setupContents
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