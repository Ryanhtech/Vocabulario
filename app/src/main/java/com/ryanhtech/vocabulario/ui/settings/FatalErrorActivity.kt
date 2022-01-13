/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.ryanhtech.vocabulario.BuildConfig
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.utils.DataManager
import com.ryanhtech.vocabulario.utils.Utils

class FatalErrorActivity : VocabularioActivity() {
    // Widgets
    private lateinit var resetButton:    ConstraintLayout
    private lateinit var continueButton: ConstraintLayout
    private lateinit var settingsButton: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the Activity
        setContentView(R.layout.activity_fatal_error)

        // Initialize the widgets
        resetButton    =    findViewById(R.id.deleteAppDataRepairAppButton)
        continueButton =    findViewById(R.id.closeRepairWizardButton)
        settingsButton =    findViewById(R.id.openAppSettingsPageButton)

        // Set the onClickListeners
        resetButton.setOnClickListener {
            confirmReset()
        }

        settingsButton.setOnClickListener {
            Utils.displayAppInfo(this, BuildConfig.APPLICATION_ID)
        }

        continueButton.setOnClickListener {
            // End the activity
            finish()
        }
    }

    private fun confirmReset() {
            runOnUiThread {
                AlertDialog.Builder(this)
                        .setTitle(R.string.delete_data_confirm_dialog)
                        .setMessage(R.string.delete_data_confirm_dialog_descr)

                        // Set the button's onClickListeners
                        .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(R.string.delete_data) { dialog: DialogInterface, _: Int ->
                            // Create an instance of the data manager and delete the data
                            dialog.dismiss()
                            DataManager().clearAppData(applicationContext)
                        }
                    .show()
        }
    }
}