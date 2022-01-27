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

package com.ryanhtech.vocabulario.ui.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.ryanhtech.vocabulario.R

/**
 * This class is an Activity that pops up from the bottom of the
 * screen. It displays useful content periodically, or asks for
 * a user action or input.
 *
 * To use an activity that implements the VocabularioPopupActivity
 * class, just call the PopupActivityExecutor.runPopupActivity()
 * method and it will do all the work for you.
 */
open class VocabularioPopupActivity : VocabularioActivity() {
    companion object {
        /**
         * This Extra must contain the parent Activity's `window.
         * decorView.rootView` value. You can call the
         * `PopupActivityExecutor.runPopupActivity()` with the
         * required arguments to run a PopupActivity without
         * having to manage all this.
         */
        const val EXTRA_PARENT_ACTIVITY_ROOTVIEW = "com.ryanhtech.vocabulario.ui.activity." +
                "VocabularioPopupActivity.EXTRA_PARENT_ACTIVITY_ROOTVIEW"
    }

    // Contains the parent Activity's root view.
    private lateinit var mParentActivityRootView: View

    // Contains the Activity's root view screenshot.
    private lateinit var mParentActivityScreenshot: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove all the transitions
        overridePendingTransition(0, 0)

        // Get the root view extra and throw an exception if it is null
        val lParentActivityRootView = intent.getSerializableExtra(EXTRA_PARENT_ACTIVITY_ROOTVIEW)
            ?: throw IllegalArgumentException("EXTRA_PARENT_ACTIVITY_ROOTVIEW is null!")
        mParentActivityRootView = lParentActivityRootView as View
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in_popup_back_transition,
            R.anim.slide_out_bottom_transition)
    }

    /**
     * This method MUST be called by any Activity that implements the
     * VocabularioPopupActivity class. It must be called after the
     * Activity has finished initialization processes
     */
    private fun startPopupAction(img: ImageView, rootView: View) {
        // Set the ImageView to the requested View

    }
}