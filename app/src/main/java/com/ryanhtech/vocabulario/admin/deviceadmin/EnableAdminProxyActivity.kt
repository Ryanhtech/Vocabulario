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