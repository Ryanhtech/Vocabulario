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

package com.ryanhtech.vocabulario.internal.legal.licensemgr

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ryanhtech.vocabulario.R
import com.scottyab.rootbeer.RootBeer

class RootCheck(pContext: Context) {
    /**
     * This private variable contains the RootBeer instance.
     */
    private var mRootBeer = RootBeer(pContext)

    /**
     * Determines if the device is rooted using the RootBeer API.
     */
    fun isDeviceRooted(): Boolean {
        // If we have already checked for root, return the result immediately. Else, check root
        // before.
        // Log the event
        Log.v(TAG, "Checking root...")

        if (sIsInstanceRooted == null) {
            // We have not checked if we are rooted. Ask RootBeer then.
            val lIsRootBeerRooted = mRootBeer.isRooted

            // Set the root status to RootBeer's result
            sIsInstanceRooted = lIsRootBeerRooted
        }

        // Log and return the result
        Log.i(TAG, "isDeviceRooted: $sIsInstanceRooted")
        return sIsInstanceRooted as Boolean
    }

    companion object {
        /**
         * This shows a Toast that indicates to the user that their device is rooted.
         */
        fun showStandaloneRootedToast(pContext: Context) {
            // Build the warning Toast
            val lWarningToast = Toast.makeText(pContext, R.string.device_rooted_standalone_warning,
                Toast.LENGTH_LONG)

            // Show the Toast
            lWarningToast.show()
        }

        /**
         * This private variable stores the status of the root for an app instance.
         */
        private var sIsInstanceRooted: Boolean? = null

        // Class TAG
        private const val TAG = "RootCheck"
    }
}