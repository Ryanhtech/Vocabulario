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

package com.ryanhtech.vocabulario.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPermissions
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.settings.subsettings.SubSettingsFragmentList
import com.ryanhtech.vocabulario.utils.DataManager
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.activity_settings_v2.*

class SettingsActivity : VocabularioActivity() {
    // Widgets
    private lateinit var mainSettingsTopTile: ConstraintLayout
    private lateinit var mainSettingsScrollView: ScrollView
    private lateinit var mainSettingsBackButton: Button

    companion object {
        private const val COLLECTION_SUB_SETTINGS = SubSettingsFragmentList.SUB_SETTINGS_COLLECTION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!checkAdmin()) {
            return
        }

        setContentView(R.layout.activity_settings_v2)

        initUi()

        /*// Initialize the widgets
        resetAppDataButton       = findViewById(R.id.resetAppDataButtonSettings)
        versionInformationButton = findViewById(R.id.versionInformationSettingsButton)

        // Set the onClickListeners
        resetAppDataButton.setOnClickListener {
            // Start the reset activity, if the device isn't managed by an organization
            startActivity(
                Intent(
                    this,
                    UserSetupActivity::class.java
                )
            )
        }

        versionInformationButton.setOnClickListener {
            // Start the version info activity
            startActivity(
                Intent(
                    this,
                    AboutAppActivity::class.java
                )
            )
        }*/
    }

    private fun initUi() {
        managedByOrganizationInfoSettings.isVisible =
            DataManager().isManagedByOrganization(this)

        mainSettingsTopTile = findViewById(R.id.mainSettingsTopTile)
        mainSettingsScrollView = findViewById(R.id.mainSettingsScrollView)
        mainSettingsBackButton = findViewById(R.id.mainSettingsBackButton)

        adaptSystemWindows()

        mainSettingsBackButton.setOnClickListener { finish() }
        displayAdminSupervisionInfoSettings.setOnClickListener {
            AdminPermissions.displaySupervisionInfo(this)
        }

        // Set the onClickListeners
        mainSettingsCollectionTile.setOnClickListener {
            throwToSubSettingsPage(COLLECTION_SUB_SETTINGS) }
    }

    private fun checkAdmin(): Boolean {
        /*if (DataManager.checkIfAppConfigured(applicationContext)
            && DataManager().isManagedByOrganization(applicationContext)
            && !AdminPermissions.adminUnlocked) {
            AdminPermissions().startProtectedActivity(SettingsActivity::class.java, this)
            finish()

            return false
        }

        else */ if (!DataManager.checkIfAppConfigured(applicationContext)) {
            AlertDialog.Builder(this)
                .setTitle(R.string.configure_app_before_settings)
                .setMessage(R.string.configure_app_before_settings_description)

                .setCancelable(false)

                .setPositiveButton(android.R.string.ok) {dialog, _ ->
                    dialog.dismiss()
                    finish()
                }

                .show()

            return false
        }

        return true
    }

    private fun adaptSystemWindows() {
        // Set the padding of the LinearLayout containing the Back button to adapt
        // to the status bar height

        val statusBarHeight = UiUtils.getStatusBarHeight(this)

        mainSettingsTopTile.let { view ->
            view.setPadding(
                view.paddingLeft,
                statusBarHeight,
                view.paddingRight,
                view.paddingBottom
            )
        }
    }

    private fun throwToSubSettingsPage(page: Int) {
        Log.v("Settings", "Throwing to SubSettings page #$page")

        startActivity(Intent(this, SubSettingsActivity::class.java).putExtra(
            SubSettingsActivity.EXTRA_SETTING, page
        ))
    }
}