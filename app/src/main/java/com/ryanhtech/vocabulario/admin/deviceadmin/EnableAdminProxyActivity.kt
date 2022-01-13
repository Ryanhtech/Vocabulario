/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.admin.deviceadmin

import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity

class EnableAdminProxyActivity : VocabularioActivity() {
    private val adminComponentName = VocabularioDeviceAdminReceiver.getComponentName()
    override val applyEmergencyBlock: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
                finishWithResult(result.resultCode == RESULT_OK)
            }

        super.onCreate(savedInstanceState)

        // Exit if admin is already enabled
        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager

        if (dpm.isAdminActive(adminComponentName)) {
            finishWithResult(true)
        }

        // Else, enable the component and ask for activation

        VocabularioDeviceAdminReceiver.changeAdminEnabledStatus(true, this)

        registerResult.launch(Intent(
            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN
        ).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                adminComponentName)
            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.device_admin_descr))
        })
    }

    private fun finishWithResult(isResultPositive: Boolean) {
        val resultCode = if (isResultPositive) RESULT_OK else RESULT_CANCELED

        setResult(resultCode)

        if (resultCode != RESULT_OK) {
            VocabularioDeviceAdminReceiver.changeAdminEnabledStatus(false, this)
        }

        finish()
    }
}