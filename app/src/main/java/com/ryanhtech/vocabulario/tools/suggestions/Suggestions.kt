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

package com.ryanhtech.vocabulario.tools.suggestions

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translator
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.tools.base.BaseTool

class Suggestions: BaseTool() {
    companion object {
        /**
         * The tool name string resource ID. It might be changed
         * over time, so we will put this in a const val.
         */
        @JvmStatic
        val TOOL_NAME_RES: Int = R.string.suggestions

        /**
         * The tag used in Logcat.
         */
        @JvmStatic
        private val TAG: String = "Suggestions"

        /**
         * Notify com.google.android.gms to download the translation
         * model and install it on the app.
         */
        @JvmStatic
        fun setupSuggestionsSignal(
            translator              : Translator,
            downloadConditions      : DownloadConditions,
            downloadFinishedAction  : Runnable? = null,
            downloadFailedAction    : Runnable? = null
        )                           : Boolean
        {
            return processSetupSuggestionsSignal(
                translator,
                downloadConditions,
                downloadFinishedAction,
                downloadFailedAction
            )
        }

        @JvmStatic
        private fun processSetupSuggestionsSignal(
            translator              : Translator,
            downloadConditions      : DownloadConditions,
            downloadFinishedAction  : Runnable? = null,
            downloadFailedAction    : Runnable? = null
        )                           : Boolean {
            translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener {
                    downloadFinishedAction?.run()
                }
                .addOnFailureListener {
                    downloadFailedAction?.run()
                }
                .addOnCanceledListener {
                    downloadFailedAction?.run()
                }

            return true
        }

        fun isSuggestionsActivated(
            translator: Translator,
            context: Context
        ): Boolean
        {
            try {
                translator.translate("hola")
            } catch (e: MlKitException) {
                if (e.errorCode == MlKitException.NOT_FOUND) return false
            } catch (e: IllegalStateException) {
                Toast.makeText(context, context.getString(R.string.cant_verify_suggestions_activation),
                        Toast.LENGTH_SHORT).show()
                return false
            } catch (e: RuntimeExecutionException) {
                return false
            }

            return true
        }
    }
}