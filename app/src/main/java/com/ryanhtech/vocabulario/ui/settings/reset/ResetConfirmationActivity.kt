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

package com.ryanhtech.vocabulario.ui.settings.reset

import android.os.Bundle
import android.util.Log
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.reset.VocabularioResetOperation
import com.ryanhtech.vocabulario.internal.reset.VocabularioResetType
import kotlinx.android.synthetic.main.activity_reset_confirmation.*

/**
 * This class provides the Activity that allows the user to confirm a
 * reset operation.
 */
class ResetConfirmationActivity : BaseResetActivity() {
    private lateinit var resetOperationInstance: VocabularioResetOperation

    private lateinit var requestedOperation: String

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
        val reqOpeFromIntent = intent.getStringExtra(EXTRA_REQUESTED_OPERATION)
        if (reqOpeFromIntent == null) {
            // The Activity hasn't started correctly.
            Log.e("ResetConfirmation", "Error starting activity: No requested " +
                "operation found in EXTRA_REQUESTED_OPERATION")
            throw IllegalStateException("No requested operation found")
        }

        requestedOperation = reqOpeFromIntent

        // Initialize the reset operation instance.
        initResetOperation()

        // Initialize the button, so that when the user clicks it, the operation
        // is launched.
        resetConfirmationRunOperationButton.setOnClickListener {
            // Run the requested operation. This can be an uninstallation,
            // a reset, or something else.
            resetOperationInstance.runOperation(this)
        }
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

        resetOperationInstance.authenticate(Thread {}, Thread { this.finish() })
    }

    private fun initResetOperation() {
        // Instantiate a new class with what we want
        val resetOpeClassInst = VocabularioResetOperation(
            requestedOperation, this)  // Bug: don't use this.applicationContext!
        resetOperationInstance = resetOpeClassInst
    }
}