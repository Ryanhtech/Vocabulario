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

package com.ryanhtech.vocabulario.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.ryanhtech.vocabulario.internal.framework.VbUtils
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity

/**
 * This class is a [Fragment] that contains all the Vocabulario utilities and implementations. You
 * should use it like this:
 *
 * ```
 * package com.ryanhtech.app.package
 *
 * import com.ryanhtech......VocabularioFragment
 * import ...
 * import ...
 *
 * class MyVocabularioFragment(): VocabularioFragment() {
 *     override fun someMethod() {
 *         workWorkWork()
 *     }
 *
 *     ...
 * }
 * ```
 */
open class VocabularioFragment : Fragment() {
    /**
     * This contains a [VbUtils] instance that matches with the Activity's context.
     */
    private var mVbUtilsInst: VbUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // First check if we actually are in a VocabularioActivity
        val lActivity: VocabularioActivity
        try {
            // This checks if we are in a VocabularioActivity, because if this fails, then the
            // Activity we are associated with is not a VocabularioActivity
            lActivity = requireActivity() as VocabularioActivity
        }
        catch (e: ClassCastException) {
            // We are not associated with a VocabularioActivity.
            Log.e("VocabularioFragment", "The associated Activity is not a "
                    + "VocabularioActivity child. Cannot initialize VocabularioFragment.")
            throw IllegalStateException("Cannot find VocabularioActivity in VocabularioActivity")
        }

        // Initialize the VbUtils instance using our VocabularioActivity
        mVbUtilsInst = initializeVocabularioUtils(lActivity)
    }

    /**
     * This initializes the [VbUtils] instance that matches with the [VocabularioActivity] you give.
     *
     * @param pContext The [VocabularioActivity] to use when initializing the [VbUtils] instance.
     * @return The initialized [VbUtils] instance.
     */
    private fun initializeVocabularioUtils(pContext: VocabularioActivity): VbUtils {
        // Initialize VbUtils in a try/catch
        val lVbUtilsInst: VbUtils

        try {
            lVbUtilsInst = VbUtils(pContext)
        }
        catch (e: Exception) {
            // Log what we were doing on the console and rethrow the exception
            var lCActivity: String? = requireActivity()::class.java.name
            if (lCActivity == null) {
                lCActivity = "null"
            }

            // Initialize the error message with the Activity that encountered the error.
            val lErrorMessage = "An error occurred during VbUtils initialization.\n\t" +
                    "Activity that encountered an error: " + lCActivity

            // Use the error message to log, and throw the exception
            Log.e("VocabularioFragment", lErrorMessage)
            throw e
        }

        // Return the VbUtils
        return lVbUtilsInst
    }

    /**
     * This returns the current Fragment's [VbUtils] instance.
     */
    private fun getVbUtils(): VbUtils {
        return mVbUtilsInst!!
    }
}