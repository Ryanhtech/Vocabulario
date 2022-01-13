/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.reset

import android.content.Context
import java.io.File

/**
 * This object contains tools to manage re-installation
 * requests.
 */
object LocalConfigurationRequest {
    private const val REQUEST_FILE_NAME = "reConfigRequest.ini"

    // TODO Class method optimization and documentation

    @JvmStatic
    fun requestReConfig(context: Context) {
        File(context.applicationContext.filesDir, REQUEST_FILE_NAME).createNewFile()
    }

    @JvmStatic
    fun deleteReConfigRequest(context: Context) {
        try {
            File(context.applicationContext.filesDir, REQUEST_FILE_NAME).delete()
        } catch (e: Exception) { }
    }

    fun isReConfigRequested(context: Context): Boolean {
        return File(context.applicationContext.filesDir, REQUEST_FILE_NAME).exists()
    }
}