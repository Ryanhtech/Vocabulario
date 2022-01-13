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

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.base.UserSetupActivity
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.utils.Utils
import kotlinx.android.synthetic.main.fragment_features_setup.view.*

class FeaturesSetupFragment : AppSetupFragment() {

    private lateinit var globalView: View
    override val nextStep: Int = UserSetupList.SETUP_SUGGESTIONS_C

    private var areGmsAvailable = true
    private var isWifiAvailable = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        globalView = inflater.inflate(R.layout.fragment_features_setup, container, false)

        /*checkGooglePlayServices()
        checkIsWiFiConnected()

        globalView.suggestionsDisabledLearnMoreButton.setOnClickListener { learnMoreInfo() }*/

        activity?.startActivity(
            Intent(requireActivity(), UserSetupActivity::class.java)
            .putExtra("step", UserSetupList.SETUP_SUGGESTIONS_C))
        activity?.finish()

        return globalView
    }

    override fun onNextPressed(): Boolean {
        // Set the Suggestion setting
        UserSetupList.configIsSuggestionsEnabled = globalView.suggestionsEnabledSetup.isChecked
        return true
    }

    private fun checkIsWiFiConnected() {
        if (Utils.isWiFiConnected(requireActivity()) || !areGmsAvailable) return

        invalidateSuggestions(requireActivity().getString(R.string.wifi_required),
            requireActivity().getString(R.string.ignore))

        isWifiAvailable = false
    }

    private fun checkGooglePlayServices() {
        val gmsStatus = GoogleApiAvailability.getInstance().
                isGooglePlayServicesAvailable(requireActivity())

        if (gmsStatus == ConnectionResult.SUCCESS) return

        // Google Play services are unavailable.
        invalidateSuggestions(requireActivity().getString(R.string.google_services_missing))

        areGmsAvailable = false
    }

    private fun invalidateSuggestions(errText: String,
                                      learnMoreText: String = requireActivity().getString(R.string.learn_more)
    ) {
        val redColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(R.color.red, requireActivity().theme)
        } else {
            resources.getColor(R.color.red)
        }

        globalView.suggestionsEnabledSetup.isEnabled = false
        globalView.suggestionsEnabledSetup.isChecked = false
        globalView.suggestionsEnableFeaturesDescription.text = errText
        globalView.suggestionsEnableFeaturesDescription.setTextColor(redColor)
        globalView.googlePlayServicesUnavailableWarningSetup.isVisible = true
        globalView.suggestionsDisabledLearnMoreButton.isVisible = true
        globalView.suggestionsDisabledLearnMoreButton.text = learnMoreText
    }

    private fun revalidateSuggestions() {
        val textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(R.color.basic_text, requireActivity().theme)
        } else {
            resources.getColor(R.color.basic_text)
        }

        globalView.suggestionsEnabledSetup.isEnabled = true
        globalView.suggestionsEnabledSetup.isChecked = true
        globalView.suggestionsEnableFeaturesDescription.text = getString(R.string.check_entries_dialog_text)
        globalView.suggestionsEnableFeaturesDescription.setTextColor(textColor)
        globalView.googlePlayServicesUnavailableWarningSetup.isVisible = false
        globalView.suggestionsDisabledLearnMoreButton.isVisible = false
    }

    private fun learnMoreInfo() {
        if (!areGmsAvailable) {
            val dialog = AlertDialog.Builder(requireActivity())
                .setTitle(R.string.learn_more)
                .setMessage(R.string.google_services_missing_descr)
                .setPositiveButton(android.R.string.ok) {_, _ -> }

            dialog.show()

            return
        }

        if (!isWifiAvailable) revalidateSuggestions()
    }
}