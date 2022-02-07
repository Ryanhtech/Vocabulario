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

package com.ryanhtech.vocabulario.ui.popup

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.ryanhtech.vocabulario.ui.viewmanager.ViewContainer

/**
 * This object helps managing PopupFragment execution.
 */
object PopupFragmentExecutor {
    /**
     * Call this to display a new PopupFragment.
     */
    fun displayPopupFragment(fragmentInst: PopupFragment, activityContext: Activity) {
        // Put the root view into a local variable
        val lActivityRootView = activityContext.window.decorView.rootView

        // Now initialize the Intent and put the string extras into it
        val lPopupIntent = Intent(activityContext, PopupContainerActivity::class.java)

        // Save the class instance and the fragment to get them back in the next Activity
        val lViewId = ViewContainer.saveInstance(lActivityRootView)
        val lFragId = ViewContainer.saveInstance(fragmentInst)

        // Set the extras
        lPopupIntent.apply {
            putExtra(PopupContainerActivity.EXTRA_FRAGMENT_TO_SET, lFragId)
            putExtra(PopupContainerActivity.EXTRA_PARENT_ACTIVITY_ROOTVIEW, lViewId)
        }

        // Start the popup Activity using our Intent
        try {
            activityContext.startActivity(lPopupIntent)
        }
        catch (err: Exception) {
            // An exception occurred while starting the Activity
            Log.e("PopupFragmentExecutor",
                "An error occurred while starting the PopupContainerActivity.")
            throw err
        }
    }
}