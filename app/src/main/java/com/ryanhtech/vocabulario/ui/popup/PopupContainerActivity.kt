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

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ryanhtech.vocabulario.R
import jp.wasabeef.blurry.Blurry

/**
 * This class is an Activity that pops up a fragment from the
 * bottom of the screen. It displays useful content periodically,
 * or asks for a user action or input.
 *
 * To use a popup fragment, use PopupFragmentExecutor.displayPopup
 * Fragment() method OR the displayPopupFragment() from a
 * VocabularioActivity.
 *
 * These popups are a bit iOS-style Sheets (see
 * https://developer.apple.com/design/human-interface-guidelines/ios/views/sheets
 * to learn more).
 */
class PopupContainerActivity : AppCompatActivity() {
    companion object {
        /**
         * This Extra must contain the parent Activity's
         * `window.decorView.rootView` value. You can call the
         * `PopupActivityExecutor.runPopupActivity()` with the
         * required arguments to run a PopupActivity without
         * having to manage all this.
         */
        const val EXTRA_PARENT_ACTIVITY_ROOTVIEW = "com.ryanhtech.vocabulario.ui.popup." +
                "PopupContainerActivity.EXTRA_PARENT_ACTIVITY_ROOTVIEW"

        /**
         * This Extra holds the fragment to be displayed.
         */
        const val EXTRA_FRAGMENT_TO_SET = "com.ryanhtech.vocabulario.ui.popup." +
                "PopupContainerActivity.EXTRA_FRAGMENT_TO_SET"
    }

    // Contains the parent Activity's RootView
    private lateinit var mParentActivityRootView: View

    // The blurry screenshot
    private lateinit var mParentActivityBlurryScreenshot: Blurry.ImageComposer

    // The fragment to inflate
    private lateinit var mFragmentToInflate: Fragment

    // Background ImageView
    private lateinit var mImageView: ImageView

    // Content LinearLayout
    private lateinit var mContentLayout: LinearLayout

    // Fragment FrameLayout
    private lateinit var mFragmentFrameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view
        setContentView(R.layout.activity_vocabulario_popup)

        // Save the screenshot as a View for now
        val lParentActivityRootView = intent.getSerializableExtra(EXTRA_PARENT_ACTIVITY_ROOTVIEW)
            ?: throw IllegalArgumentException("EXTRA_PARENT_ACTIVITY_ROOTVIEW is null!")
        mParentActivityRootView = lParentActivityRootView as View

        // Save the fragment to display
        val lFragmentFromIntent = intent.getSerializableExtra(EXTRA_FRAGMENT_TO_SET)
            ?: throw IllegalArgumentException("EXTRA_FRAGMENT_TO_SET is null!")
        mFragmentToInflate = lFragmentFromIntent as Fragment

        // Remove transitions (if you pass 0 on both parameters it will remove everything)
        overridePendingTransition(0, 0)

        // Then, initialize the widgets
        mImageView = findViewById(R.id.popupActivityBackImageView)
        mContentLayout = findViewById(R.id.popupContentLinearLayout)
        mFragmentFrameLayout = findViewById(R.id.popupFragmentContentFrameLayout)

        // Get the blurry image
        val lBlurryScreenshot = Blurry.with(this).capture(mParentActivityRootView)
        mParentActivityBlurryScreenshot = lBlurryScreenshot

        // Set the ImageView image to the blurry image
        mParentActivityBlurryScreenshot.into(mImageView)

        // Now inflate the fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.subSettingsFragment, lFragmentFromIntent)
            commit()
        }

        // Start the ultimate popup animation
        startPopupAnimation()
    }

    private fun startPopupAnimation() {
        // TODO Animation
    }
}