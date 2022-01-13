/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.block

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import kotlinx.android.synthetic.main.activity_forgot_password_block_prompt.*

class ForgotPasswordBlockPromptActivity : VocabularioActivity() {
    override val applyEmergencyBlock = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_block_prompt)

        forgotPasswordLayout.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.zoom_in
            )
        )

        forgotPwdBlock.setOnClickListener {
            showConfirmDialog()
        }
    }

    override fun onBackPressed() {
        forgotPasswordLayout.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.zoom_out
            )
        )

        Thread {
            Thread.sleep(100)

            runOnUiThread {
                super.onBackPressed()
                overridePendingTransition(0, 0)
            }
        }.start()
    }

    private fun startBlockMode() {
        AdminPasswordManager.startEmergencyBlockMode(applicationContext)
    }

    private fun showConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_block_confirm)
            .setMessage(R.string.dialog_block_confrm_d)

            .setPositiveButton(android.R.string.ok) {dialog, _ ->
                dialog.dismiss()
                startBlockMode()
            }

            .setNegativeButton(android.R.string.cancel) {dialog, _ ->
                dialog.dismiss()
                onBackPressed()
            }

            .setOnCancelListener {
                onBackPressed()
            }

            .show()
    }
}