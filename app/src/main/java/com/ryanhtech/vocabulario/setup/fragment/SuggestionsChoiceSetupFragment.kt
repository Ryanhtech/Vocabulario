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

import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import kotlinx.android.synthetic.main.fragment_setup_suggestions_choice.view.*

class SuggestionsChoiceSetupFragment : AppSetupFragment() {
    override val nextStep: Int = UserSetupList.SETUP_FEATURES_INSTALL

    override val fragmentLayout: Int = R.layout.fragment_setup_suggestions_choice
    override val fragmentTitleResource: Int = R.string.activate_suggestions_settings
    override val fragmentDescriptionResource: Int = R.string.suggestions_main_feature_descr
    override val fragmentIconResource: Int = R.drawable.ic_baseline_assistant_24

    override fun onNextPressed(): Boolean {
        super.onNextPressed()
        UserSetupList.configIsSuggestionsEnabled = globalView!!.isSuggestionsEnabledV2.isChecked

        return true
    }
}