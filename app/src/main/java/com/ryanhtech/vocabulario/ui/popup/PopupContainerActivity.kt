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
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.ui.viewmanager.ViewContainer
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
    private lateinit var mBackgroundView: View

    // Content LinearLayout
    private lateinit var mContentLayout: LinearLayout

    // Fragment FrameLayout
    private lateinit var mFragmentFrameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view
        setContentView(R.layout.activity_vocabulario_popup)

        // Convert everything from JSON
        val lGsonInst = Vocabulario.getGson()

        // Save the screenshot as a View for now
        val lParentActivityRootViewJson = intent.getIntExtra(EXTRA_PARENT_ACTIVITY_ROOTVIEW, -1)

        if (lParentActivityRootViewJson == -1) {
            throw IllegalArgumentException("EXTRA_PARENT_ACTIVITY_ROOTVIEW can't be found!")
        }

        mParentActivityRootView = ViewContainer.getInstance(lParentActivityRootViewJson) as View

        // Perform the same operation on the fragment to set
        val lFragmentFromIntent = intent.getIntExtra(EXTRA_FRAGMENT_TO_SET, -144)

        if (lFragmentFromIntent == -144) {
            throw IllegalArgumentException("Can't found EXTRA_FRAGMENT_TO_SET")
        }

        mFragmentToInflate = ViewContainer.getInstance(lFragmentFromIntent) as Fragment

        // Remove transitions (if you pass 0 on both parameters it will remove everything)
        overridePendingTransition(0, 0)

        // Then, initialize the widgets
        mBackgroundView = findViewById(R.id.popupActivityBackgroundView)
        mContentLayout = findViewById(R.id.popupContentLinearLayout)
        mFragmentFrameLayout = findViewById(R.id.popupFragmentContentFrameLayout)

        // Get the blurry image
        val lBlurryScreenshot = Blurry.with(this).capture(mParentActivityRootView)
        mParentActivityBlurryScreenshot = lBlurryScreenshot

        // Set the ImageView image to the blurry image
        //mParentActivityBlurryScreenshot.into(mImageView)
        // TODO: Update screenshot (keep it or remove it?)

        // Now inflate the fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.popupFragmentContentFrameLayout, mFragmentToInflate)
            commit()
        }

        // Run the animation after the mContentLayout has been drawn on the screen
        mContentLayout.doOnLayout {
            startPopupAnimationThread()
        }
    }

    private fun startPopupAnimationThread() {
        // Create the thread and start it
        val lAnimThread = Thread { startPopupAnimation() }
        lAnimThread.start()
    }

    private fun startPopupAnimation() {
        // Instantiate a new SpringAnimation
        val lSprAnim = SpringAnimation(mContentLayout, DynamicAnimation.TRANSLATION_Y,
            mContentLayout.y).apply {
                spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                spring.stiffness = SpringForce.STIFFNESS_MEDIUM
        }

        runOnUiThread {
            // Move the View down a bit
            // For some reason you have to use + instead of -
            mContentLayout.y = mContentLayout.y + mContentLayout.measuredHeight

            // Run the animation
            lSprAnim.start()

            // Show the content layout because it is hidden by default
            mContentLayout.visibility = View.VISIBLE
        }

        // Now, prepare the background animation
        // The aim of this animation is to make the user feel like it's switching
        // in another world.
        val lPopupBackgroundAnimationInst = AnimationUtils.loadAnimation(this,
            R.anim.popup_anim_background_fadein)

        // Set the animation listeners to detect the end of the animation and to keep the background
        // transparent after the animation
        lPopupBackgroundAnimationInst.setAnimationListener(object: Animation.AnimationListener {
            // You don't need to implement these
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Check if we have the right animation
                if (lPopupBackgroundAnimationInst != animation) {
                    Log.w("PopupContainerActivity", "The animation in AnimationListener " +
                            "is not right.")
                    return
                }

                // Then set the background's alpha to the right one
                mBackgroundView.alpha = 0.9F
            }
        })

        // Start the fade in animation
        mBackgroundView.startAnimation(lPopupBackgroundAnimationInst)
    }
}