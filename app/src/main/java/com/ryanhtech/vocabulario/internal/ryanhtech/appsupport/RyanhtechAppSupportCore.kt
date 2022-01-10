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

package com.ryanhtech.vocabulario.internal.ryanhtech.appsupport

import android.content.Context
import android.widget.Toast
import com.ryanhtech.vocabulario.internal.ryanhtech.webdb.FirebaseUtil
import com.ryanhtech.vocabulario.internal.ryanhtech.webdb.FirebaseWebDbKeys
import com.ryanhtech.vocabulario.utils.Utils
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.IOException

class RyanhtechAppSupportCore(context: Context) {
    /**
     * This class manages the support of the genuine Ryanhtech app. It
     * won't work on variants.
     */

    companion object {
        var isAppSupportedGlobal = true
    }

    private val mContext = context

    fun installCore() = runBlocking {
        // First, check if we are supported

        val isSupportedAsync = async (start = CoroutineStart.LAZY) { checkIsAppSupported() }

        // Check if we have an Internet connection first. If
        // we haven't got it, we can cancel everything.
        // It is highly recommended to catch the exception in case we
        // cancel everything, it would be sad to crash the app because
        // no Internet connection was found.
        if (!Utils.isInternetConnected(mContext))
            throw IOException("No Internet connection: can't perform app support check")

        // We can wake up the lazy async now
        isSupportedAsync.start()

        // Wait until we have an answer from this lazy boy
        // and take action
        isSupportedAsync.await()
        val isAppSupported: Boolean = isAppSupportedGlobal

        // If we are not supported, we can take immediate action
        // to inform the user

        Toast.makeText(mContext, "isAppSupported: $isAppSupported", Toast.LENGTH_SHORT)
            .show()
    }

    suspend fun checkIsAppSupported() {
        // First, get the Firebase instance
        val db = FirebaseUtil.getFirestore()

        db.collection(FirebaseWebDbKeys.GLOBAL_COLLECTION).get()
            .addOnFailureListener {
                // Cancel everything
                return@addOnFailureListener
            }
            .addOnSuccessListener { documents ->
                // Parse every document to find the global config one
                for (document in documents) {
                    if (document.id == FirebaseWebDbKeys.GLOBAL_CONFIG_KEY) {
                        // We got our document. Now we can scan it and
                        // take action depending on its content.
                        isAppSupportedGlobal =
                            document.getBoolean(FirebaseWebDbKeys.IS_APP_SUPPORTED_VALUE)
                                ?: false  // If we haven't our value it means that the server is dead

                        // Return
                        return@addOnSuccessListener
                    }
                }
            }
    }
}