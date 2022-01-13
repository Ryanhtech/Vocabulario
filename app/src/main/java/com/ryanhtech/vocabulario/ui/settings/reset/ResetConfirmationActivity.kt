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

package com.ryanhtech.vocabulario.ui.settings.reset

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.reset.VocabularioResetOperation
import com.ryanhtech.vocabulario.internal.reset.VocabularioResetType
import com.ryanhtech.vocabulario.internal.reset.VocabularioUserResetOperationInformation
import com.ryanhtech.vocabulario.ui.animations.VocabularioListAnimation
import kotlinx.android.synthetic.main.activity_reset_confirmation.*

/**
 * This class provides the Activity that allows the user to confirm a
 * reset operation.
 */
class ResetConfirmationActivity : BaseResetActivity() {
    private lateinit var resetOperationInstance: VocabularioResetOperation
    private lateinit var requestedOperation: String

    private lateinit var onScreenViewsAnimation: VocabularioListAnimation
    private lateinit var pendingEmptyDialog: AlertDialog

    private var ignoreAuthenticationOnResume = false

    companion object {
        const val EXTRA_REQUESTED_OPERATION = "requestedOperation"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view so the user sees our views on the
        // screen
        setContentView(R.layout.activity_reset_confirmation)

        // Set the requestedOperation to the operation that the user
        // requested.
        val reqOpeFromIntent = intent.getSerializableExtra(EXTRA_REQUESTED_OPERATION)
            as VocabularioUserResetOperationInformation
        if (reqOpeFromIntent.operationTypeLocal == "") {
            // The Activity hasn't started correctly.
            Log.e("ResetConfirmation", "Error starting activity: No requested " +
                "operation found in EXTRA_REQUESTED_OPERATION")
            throw IllegalStateException("No requested operation found")
        }

        requestedOperation = reqOpeFromIntent.operationTypeLocal

        // Initialize the UI: set the text views to the correct content.
        val resetConfirmationTitle = findViewById<TextView>(R.id.resetConformationTitle)
        val resetConfirmationDescr = findViewById<TextView>(R.id.resetConfirmationDescription)
        val actionButtonsToolbar = findViewById<LinearLayout>(R.id.resetConfirmationActionButtonToolbar)

        resetConfirmationTitle.text = getString(reqOpeFromIntent.operationNameUiLocal)
        resetConfirmationDescr.text = getString(reqOpeFromIntent.operationDescriptionUiLocal)

        // Initialize the views animation
        val lViewsToAnimate = listOf<View>(resetConfirmationTitle, resetConfirmationDescr,
            actionButtonsToolbar)
        onScreenViewsAnimation = VocabularioListAnimation(lViewsToAnimate, this)

        // Don't forget to hide the widgets
        onScreenViewsAnimation.hideViews()

        // Initialize the reset operation instance.
        initResetOperation()

        // Initialize the pending empty alert dialog.
        pendingEmptyDialog = AlertDialog.Builder(this).apply {
            setCancelable(false)
        }.create()  // create(): returns the AlertDialog from the Builder

        // Initialize the button, so that when the user clicks it, the operation
        // is launched.
        resetConfirmationRunOperationButton.setOnClickListener {
            // Show an empty dialog to prevent the user from interacting
            // with the Activity
            pendingEmptyDialog.show()

            // Run the requested operation. This can be an uninstallation,
            // a reset, or something else.
            resetOperationInstance.runOperation(this)

            // Wait 1 second to dismiss the empty pending dialog
            Handler(Looper.getMainLooper()).postDelayed({ handleOperationEnd() }, 1000)
        }

        // Check if the user requested an uninstall operation. If so, start the
        // animation directly because no prompt will be shown.
        if (requestedOperation == VocabularioResetType.TYPE_RESET_UNINSTALL) {
            startWidgetsAnimation()
        }

        // Set the fade in animation
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()

        if (ignoreAuthenticationOnResume) return
        ignoreAuthenticationOnResume = true

        // Skip the authentication if the user requested an uninstall, it
        // is not required.
        if (requestedOperation == VocabularioResetType.TYPE_RESET_UNINSTALL) {
            return
        }

        resetOperationInstance.authenticate(Thread { this.startWidgetsAnimation() },
            Thread { this.finish() })
    }

    private fun handleOperationEnd() {
        pendingEmptyDialog.dismiss()
        Toast.makeText(this, R.string.reset_operation_finished, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun startWidgetsAnimation() {
        // Start the widgets animation
        onScreenViewsAnimation.startVlaAnimation()
    }

    private fun initResetOperation() {
        // Instantiate a new class with what we want
        val resetOpeClassInst = VocabularioResetOperation(
            requestedOperation, this)  // Bug: don't use this.applicationContext!
        resetOperationInstance = resetOpeClassInst
    }
}