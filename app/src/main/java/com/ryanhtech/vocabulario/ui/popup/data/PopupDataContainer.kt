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

package com.ryanhtech.vocabulario.ui.popup.data

import android.util.Log
import com.ryanhtech.vocabulario.ui.popup.data.PopupDataContainer.Companion.READ_MODE_IN
import com.ryanhtech.vocabulario.ui.popup.data.PopupDataContainer.Companion.READ_MODE_OUT
import java.io.Serializable

/**
 * The [PopupDataContainer] is the container for all data that you pass into a Popup. When you
 * initialize it, it defaults to the [READ_MODE_IN] mode, but when you pass it to a PopupFragment
 * and launch it, it becomes [READ_MODE_OUT].
 */
class PopupDataContainer : Serializable {
    companion object {
        /**
         * This mode is used when a [PopupDataContainer] is initialized and has not passed through
         * a PopupFragment yet.
         */
        const val READ_MODE_IN = 1

        /**
         * This mode is used when a [PopupDataContainer] has passed through a PopupFragment. You
         * should use it to retrieve data from the PopupFragment, but no one forbids you from
         * adding values, even if it's useless.
         */
        const val READ_MODE_OUT = 2

        /**
         * The tag you use when you are performing calls to the [Log] class.
         */
        private const val TAG = "PopupDataContainer"
    }

    /**
     * This is the set that contains the popup data as a key/value association. You should use the
     * addValue() method to put a value into this set. You can then retrieve it using the getValue()
     * method.
     */
    private val mDataMap = mutableMapOf<CharSequence, Any>()

    /**
     * This is the current read mode. It helps you determine how to handle the data provided by
     * this [PopupDataContainer]. It can be either [READ_MODE_IN] or [READ_MODE_OUT].
     */
    val readMode = READ_MODE_IN

    /**
     * This allows you to put a value into the [PopupDataContainer].
     *
     * @param pValueIdentifier The identifier you want to use to retrieve the value you have
     * associated with it.
     * @param pValue The actual value you want to associate the identifier to.
     */
    fun addValue(pValueIdentifier: CharSequence, pValue: Any) {
        // Execute all this in a try/catch
        try {
            mDataMap[pValueIdentifier] = pValue
        }
        catch (lError: Exception) {
            // Log this miraculous event into Logcat. Even if we are rethrowing this after, it helps
            // debugging.
            Log.e(TAG, "ERROR while putting value to map with ID \"$pValueIdentifier\".")

            // Rethrow the exception
            throw lError
        }
    }

    /**
     * This allows you to get a value from the value map.
     *
     * @param pValueIdentifier The value ID you want to get.
     * @throws NoSuchFieldException If the value associated with your identifier was not found.
     */
    fun getValue(pValueIdentifier: CharSequence): Any {
        // The operation is quite simple if you don't face any error.
        val lNullableData = mDataMap[pValueIdentifier]

        // Now if we aren't null, return
        if (lNullableData != null) {
            return lNullableData
        }

        // We are null. Throw an exception by initializing an error message first
        val lErrorMessage = "ERROR: the value associated with ID \"$pValueIdentifier\" can't be" +
                " found!"

        // Log this error into Logcat
        Log.e(TAG, lErrorMessage)

        // Throw a new exception that we initialize using the error message we initialized earlier
        throw NoSuchFieldException(lErrorMessage)
    }
}