/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.setup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.setup.config.UserSetupStatus
import com.ryanhtech.vocabulario.internal.ryanhtech.familylink.FamilyLinkScan
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