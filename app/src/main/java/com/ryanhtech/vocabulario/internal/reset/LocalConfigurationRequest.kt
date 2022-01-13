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