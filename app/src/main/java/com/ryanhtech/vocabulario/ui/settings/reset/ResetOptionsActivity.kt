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

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.reset.VocabularioResetType
import com.ryanhtech.vocabulario.internal.reset.VocabularioUserResetOperationInformation
import com.ryanhtech.vocabulario.ui.animations.VocabularioListAnimation

/**
 * This Activity displays a list of reset options to the user. It
 * lets the user decide which option they want, and then starts
 * the ResetConfirmationActivity to ask the permission to the user
 * to perform this.
 *
 * @see ResetConfirmationActivity
 * @see BaseResetActivity
 * @author Ryanhtech Labs
 * @since initial version
 */
class ResetOptionsActivity : BaseResetActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_options)

        // Initialize the UI components. These are initialized using the
        // findViewById() method.
        val resetOperationResetCollection: FrameLayout = findViewById(
            R.id.resetOperationResetCollection)
        val resetOperationResetAppLocal: FrameLayout = findViewById(
            R.id.resetOperationResetAppLocal)
        val resetOperationResetAppSystem: FrameLayout = findViewById(
            R.id.resetOperationResetAppSystem)
        val resetOperationUninstallApplication: FrameLayout = findViewById(
            R.id.resetOperationUninstallApplication)

        // Now, set the onClickListeners to perform on-click actions.
        resetOperationResetCollection.setOnClickListener {
            processClickListener(VocabularioResetType.TYPE_RESET_COLLECTION)
        }
        resetOperationResetAppLocal.setOnClickListener {
            processClickListener(VocabularioResetType.TYPE_RESET_LOCAL)
        }
        resetOperationResetAppSystem.setOnClickListener {
            processClickListener(VocabularioResetType.TYPE_RESET_SYSTEM)
        }
        resetOperationUninstallApplication.setOnClickListener {
            processClickListener(VocabularioResetType.TYPE_RESET_UNINSTALL)
        }

        // Here, start the VocabularioListAnimation on our items (debug)
        val viewsToAnimate = listOf<View>(resetOperationResetCollection, resetOperationResetAppLocal,
            resetOperationResetAppSystem, resetOperationUninstallApplication)

        // Create a new Vocabulario ListAnimation instance
        val vla = VocabularioListAnimation(viewsToAnimate, this)

        // Start the animation and change the Activity transition
        vla.startAnimation()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun processClickListener(resetType: String) {
        // Create a new operation info instance
        val resetOperationInfoInstance = VocabularioUserResetOperationInformation
            .getInstanceFromResetOperationType(resetType)

        // There you have the info instance. Pass it into the system call method
        internalRunResetOperationSystemCall(resetOperationInfoInstance)
    }

    /**
     * This runs an internal Vocabulario system call to perform the selected
     * reset operation. (private method)
     */
    private fun internalRunResetOperationSystemCall(ope: VocabularioUserResetOperationInformation) {
        // Create an Intent to the reset confirmation activity, add the requested operation
        // to the Intent, and start the Intent
        val confirmIntent = Intent(this, ResetConfirmationActivity::class.java)
        confirmIntent.putExtra(ResetConfirmationActivity.EXTRA_REQUESTED_OPERATION, ope)
        startActivity(confirmIntent)
    }
}