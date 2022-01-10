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

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.base.UserSetupActivity
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.setup.config.UserSetupStatus
import com.ryanhtech.vocabulario.tools.collection.CollectionManager
import com.ryanhtech.vocabulario.tools.collection.wordpointers.CollectionWordContentManager
import com.ryanhtech.vocabulario.tools.suggestions.Suggestions
import com.ryanhtech.vocabulario.utils.DataManager
import com.ryanhtech.vocabulario.utils.Utils

class FeaturesInstallFragment : AppSetupFragment() {

    private lateinit var globalView: View

    override val displayBackButton = false
    override val displayNextButton = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        globalView = inflater.inflate(R.layout.fragment_features_install, container, false)

        if (checkInternetConnection()) setupSuggestions()

        // If Suggestions are disabled, set up the Collection and immediately restart the app.
        if (!UserSetupList.configIsSuggestionsEnabled) {
            setupCollection()
            setupSystem()

            finishSetup()
        }

        return globalView
    }

    private fun checkInternetConnection(): Boolean {
        if (!Utils.isInternetConnected(requireActivity().applicationContext) && isSuggestionsEnabled()) {
            // If the Internet isn't connected and if Suggestions are enabled

            showBackRetryDialog(
                titleId       =   R.string.no_internet,
                descriptionId =   R.string.no_internet_install_desc
            )
        }
        if (Utils.isInternetConnected(requireActivity().applicationContext) && isSuggestionsEnabled()) {
            return true
        }
        return false
    }

    private fun setupSuggestions() {
        // Set up Suggestions.

        val downloadOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(TranslateLanguage.FRENCH)
            .build()

        // Get the translator from the options
        val translator = Translation.getClient(downloadOptions)

        val downloadConditions = DownloadConditions.Builder().build()

        // Start the download
        Suggestions.setupSuggestionsSignal(translator, downloadConditions, {
                finishSetup()
            }, {
            showBackRetryDialog(
                titleId       = R.string.download_failed,
                descriptionId = R.string.download_failed_description
            )
        })
    }

    private fun showBackRetryDialog(
                                    titleId: Int,
                                    descriptionId: Int
    ) {
        // Create a Dialog

        AlertDialog.Builder(activity)
            .setTitle(titleId)
            .setMessage(descriptionId)

            .setPositiveButton(R.string.retry) { _, _ ->
                // Restart the activity

                requireActivity().startActivity(
                    Intent(
                        activity,
                        UserSetupActivity::class.java
                    ).putExtra("step", UserSetupList.SETUP_FEATURES_INSTALL)
                )

                requireActivity().finish()
            }

            .setNegativeButton(R.string.back) { _, _ ->
                requireActivity().finish()
            }

            .setCancelable(false)
            .show()
    }

    private fun isSuggestionsEnabled(): Boolean {
        return UserSetupList.configIsSuggestionsEnabled
    }

    private fun finishSetup() {
        // Make a delay to be more impressive

        Handler(Looper.getMainLooper()).postDelayed({
                DataManager.markSetupAsComplete(requireActivity().applicationContext)
                Utils().restartApp(requireActivity().applicationContext)
                requireActivity().finishAndRemoveTask()
            }, 2000
        )
    }

    private fun setupCollection() {
        val context = requireActivity()

        CollectionManager.setup(context)
        CollectionWordContentManager.initFile(context)
    }

    private fun setupSystem() {
        val adminPassword = UserSetupStatus.adminPassword
        if (adminPassword != "") {
            AdminPasswordManager.setPassword(
                requireActivity().applicationContext,
                adminPassword
            )
        }

        val orgName = UserSetupStatus.orgName
        if (orgName != "") {
            DataManager().setOrganizationName(
                orgName, requireActivity().applicationContext)
        }
    }
}