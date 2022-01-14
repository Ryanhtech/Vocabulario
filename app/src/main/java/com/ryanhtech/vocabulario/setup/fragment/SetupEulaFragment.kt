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

import android.content.Intent
import android.net.Uri
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import kotlinx.android.synthetic.main.fragment_setup_eula.view.*

class SetupEulaFragment : AppSetupFragment() {
    override val nextStep = UserSetupList.SETUP_PLACE

    override val fragmentLayout: Int = R.layout.fragment_setup_eula
    override val fragmentTitleResource: Int = R.string.policies_title
    override val fragmentIconResource: Int = R.drawable.ic_round_format_list_bulleted_24
    override val fragmentDescriptionResource: Int = R.string.eula_description

    override fun startJob() {
        globalView!!.setupEulaFragmentLicenseButton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.apache_license_url))
                )
            )
        }
    }
}