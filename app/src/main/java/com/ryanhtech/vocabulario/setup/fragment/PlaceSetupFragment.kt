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
import com.ryanhtech.vocabulario.setup.AppSetupFragment
import com.ryanhtech.vocabulario.setup.UserSetupList
import com.ryanhtech.vocabulario.setup.UserSetupStatus
import com.ryanhtech.vocabulario.system.ryanhtech.familylink.FamilyLinkScan
import kotlinx.android.synthetic.main.fragment_place_setup.view.*

class PlaceSetupFragment : AppSetupFragment() {
    override val displayBackButton = false
    override var nextStep: Int = UserSetupList.SETUP_ID_PERS

    private lateinit var globalView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        globalView = inflater.inflate(R.layout.fragment_place_setup, container, false)

        scanFamilyLinkAndDisplayResult()

        return globalView
    }

    override fun onNextPressed(): Boolean {
        /**
         * Check if the school option has been selected.
         */

        nextStep = if (globalView.schoolRadioButtonChoice.isChecked) {
            UserSetupList.ORG_SETUP_ADMIN_INTRO
        } else {
            UserSetupList.SETUP_ID_PERS
        }

        if (nextStep == UserSetupList.SETUP_ID_PERS) {
            UserSetupStatus.adminPassword = ""  // This fixes a bug in which the program
                                                // tells to the user that it is managed
                                                // by his organization even if he has set
                                                // up Vocabulario for himself/herself
        }

        return true
    }

    /**
     * This prevents the user to enable the organization management
     * if its device is managed with Family Link, because the user might
     * be a very young kid and might struggle to uninstall or access to
     * Vocabulario if it sets it up in managed-mode.
     */
    private fun scanFamilyLinkAndDisplayResult() {
        val familyLinkScanInstance = FamilyLinkScan(requireActivity().applicationContext)

        if (!familyLinkScanInstance.isDeviceFamilyLinked()) return

        // The device is "Family Linked". We must disable the managed option and show
        // a message to the user.
        globalView.schoolRadioButtonChoice.isEnabled = false
        globalView.schoolRadioButtonChoice.alpha = 0.5F
        globalView.schoolRadioButtonChoiceDescription.text = requireActivity()
            .getString(R.string.family_link_managed_mode_unavailable)
    }
}