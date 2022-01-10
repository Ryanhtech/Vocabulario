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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.utils.DataManager
import kotlinx.android.synthetic.main.fragment_enter_identity_setup.*

class SetupFinishedFragment : AppSetupFragment() {

    override val nextStep = UserSetupList.SETUP_LEVEL_PERS

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_identity_setup, container, false)
    }

    override fun onNextPressed(): Boolean {
        DataManager.setName(requireActivity().applicationContext, id_fragment_name.text.toString())

        return true
    }
}