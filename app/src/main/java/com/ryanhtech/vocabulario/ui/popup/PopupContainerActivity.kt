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

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
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
class PopupContainerActivity : VocabularioActivity() {
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

        /**
         * If you should keep the finish method default behavior *(not recommended)*.
         */
        const val SHOULD_KEEP_DEFAULT_FINISH_BEHAVIOR = false
    }

    // Contains the parent Activity's RootView
    private lateinit var mParentActivityRootView: View

    // The blurry screenshot
    private lateinit var mParentActivityBlurryScreenshot: Blurry.ImageComposer

    // The fragment to inflate
    private lateinit var mFragmentToInflate: PopupFragment

    // Background ImageView
    private lateinit var mBackgroundView: View

    // Content LinearLayout
    private lateinit var mContentLayout: LinearLayout

    // Fragment FrameLayout
    private lateinit var mFragmentFrameLayout: FrameLayout

    // Fragment title TextView
    private lateinit var mFragmentTitleTextView: TextView

    // Negative button
    private lateinit var mFragmentNegativeButton: Button

    // Positive button
    private lateinit var mFragmentPositiveButton: Button

    // Left title TextView padding View
    private lateinit var mTitleTextViewLeftPaddingView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view
        setContentView(R.layout.activity_vocabulario_popup)

        // Save the screenshot as a View for now
        val lParentActivityRootViewId = intent.getIntExtra(EXTRA_PARENT_ACTIVITY_ROOTVIEW, -1)

        if (lParentActivityRootViewId == -1) {
            throw IllegalArgumentException("EXTRA_PARENT_ACTIVITY_ROOTVIEW can't be found!")
        }

        mParentActivityRootView = ViewContainer.getInstance(lParentActivityRootViewId) as View

        // Perform the same operation on the fragment to set
        val lFragmentFromIntent = intent.getIntExtra(EXTRA_FRAGMENT_TO_SET, -144)

        if (lFragmentFromIntent == -144) {
            throw IllegalArgumentException("Can't found EXTRA_FRAGMENT_TO_SET")
        }

        // Get the PopupFragment instance under a try/catch
        try {
            mFragmentToInflate = ViewContainer.getInstance(lFragmentFromIntent) as PopupFragment
        } catch (err: ClassCastException) {
            // We have been unable to cast the class. It means that the class that has been passed
            // to us isn't a PopupFragment.
            Log.w("PopupContainerActivity", "Can't cast Any to PopupFragment (it " +
                "usually means that you haven't passed a PopupFragment in the  " +
                "EXTRA_FRAGMENT_TO_SET extra. Please use the PopupFragmentExecutor API.")
            throw err
        }

        // Remove transitions (if you pass 0 on both parameters it will remove everything)
        overridePendingTransition(0, 0)

        // Then, initialize the widgets
        mBackgroundView = findViewById(R.id.popupActivityBackgroundView)
        mContentLayout = findViewById(R.id.popupContentLinearLayout)
        mFragmentFrameLayout = findViewById(R.id.popupFragmentContentFrameLayout)
        mFragmentTitleTextView = findViewById(R.id.popupTitle)
        mFragmentNegativeButton = findViewById(R.id.cancelPopupButton)
        mFragmentPositiveButton = findViewById(R.id.okPopupButton)
        mTitleTextViewLeftPaddingView = findViewById(R.id.popupTitleStartPadding)

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

        // Set the fragment info on the screen
        internalInitPopupInformation()

        // Set the button listeners
        setViewEventListeners()
    }

    override fun finish() {
        // First start the finish super method.
        super.finish()

        // Delete the transition, only if you should not keep the default behavior.
        if (SHOULD_KEEP_DEFAULT_FINISH_BEHAVIOR) {
            return
        }

        // 0 means nothing
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        // Delete the super method that will calls the standard finish, and call the cleanup method.
        finishPopup()
    }

    private fun startPopupAnimationThread(enterAnimation: Boolean = true) {
        // Create the thread and start it
        val lAnimThread = Thread { startPopupAnimation(enterAnimation) }
        lAnimThread.start()
    }

    private fun startPopupAnimation(enterAnimation: Boolean = true) {
        // Determine which values you should use depending on the enterAnimation parameter.
        var lSpringAnimationTargetLocation = mContentLayout.y
        if (!enterAnimation) {
            lSpringAnimationTargetLocation = mContentLayout.y + mContentLayout.measuredHeight
        }

        var lSpringAnimationStartY = mContentLayout.y + mContentLayout.measuredHeight
        if (!enterAnimation) {
            lSpringAnimationStartY = mContentLayout.y
        }

        var lBackgroundAnimationRes = R.anim.popup_anim_background_fadein
        if (!enterAnimation) {
            lBackgroundAnimationRes = R.anim.popup_anim_background_fadeout
        }

        var lBackgroundFinalAlpha = 1F
        if (!enterAnimation) {
            lBackgroundFinalAlpha = 0F
        }

        // Instantiate a new SpringAnimation
        val lSprAnim = SpringAnimation(mContentLayout, DynamicAnimation.TRANSLATION_Y,
            lSpringAnimationTargetLocation).apply {
                spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                spring.stiffness = SpringForce.STIFFNESS_MEDIUM
        }

        runOnUiThread {
            // Move the View down a bit
            // For some reason you have to use + instead of -
            mContentLayout.y = lSpringAnimationStartY

            // Run the animation
            lSprAnim.start()

            // Show the content layout because it is hidden by default
            mContentLayout.visibility = View.VISIBLE
        }

        // Now, prepare the background animation
        // The aim of this animation is to make the user feel like it's switching
        // in another world.
        val lPopupBackgroundAnimationInst = AnimationUtils.loadAnimation(this,
            lBackgroundAnimationRes)

        // Set the animation listeners to detect the end of the animation and to keep the background
        // transparent after the animation
        lPopupBackgroundAnimationInst.setAnimationListener(object: Animation.AnimationListener {
            // You don't need to implement these two
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
                mBackgroundView.alpha = lBackgroundFinalAlpha

                // If you are doing the fade out animation, finish the Activity now.
                if (!enterAnimation) {
                    finish()
                }
            }
        })

        Log.i("PopupContainerActivity", "Starting fade in animation")

        // Start the fade in animation inside the UI thread
        runOnUiThread {
            mBackgroundView.startAnimation(lPopupBackgroundAnimationInst)
            mBackgroundView.visibility = View.VISIBLE
        }
    }

    /**
     * This will initialize the PopupFragment's information on the screen, like the title, the
     * button's text, and more.
     */
    private fun internalInitPopupInformation() {
        // Get the context to use with the fragment
        // Caution: use only the application's Context to avoid security issues
        val lContext = applicationContext

        // First set the title
        val lFragmentTitle = mFragmentToInflate.getTitleText(lContext)
        if (lFragmentTitle == null || lFragmentTitle == "") {
            // There is no title to display, so hide the TextView.
            mFragmentTitleTextView.visibility = View.GONE
        }
        else {
            mFragmentTitleTextView.text = lFragmentTitle
        }

        if (lFragmentTitle == "") {
            // Display a warning - it isn't correct to return "" to this Activity, you should return
            // null instead
            Log.w("PopupContainerActivity", "Do not use \"\" as a title to your Popup" +
                "Fragment, return null instead")
        }

        // Determine which visibility we should apply to the button. If the button is gone, align
        // the title text to the left, and show the padding container (invisible to the user)
        var lNegativeButtonVisibility = View.VISIBLE
        if (!mFragmentToInflate.hasNegativeButton) {
            lNegativeButtonVisibility = View.INVISIBLE
        }

        // Set the visibility
        mFragmentNegativeButton.visibility = lNegativeButtonVisibility

        // Now we should set the text of the negative button
        // If the button is invisible all this has no effect to the user
        val lNegButtonText = mFragmentToInflate.getNegativeButtonText(lContext)
        mFragmentNegativeButton.text = lNegButtonText

        // Set the text and the color of the positive button
        val lPositiveButtonText = mFragmentToInflate.getPositiveButtonText(lContext)
        val lPositiveButtonColorRes = mFragmentToInflate.posButtonTintRes

        // The text is quite simple to set
        mFragmentPositiveButton.text = lPositiveButtonText

        // Now get the color depending on the Android version (compatibility stuff)
        val lButtonTint =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ContextCompat.getColor(this, lPositiveButtonColorRes)
            } else {
                // We can remove deprecation warnings since this will only be run on supported
                // platforms
                @Suppress("DEPRECATION")
                resources.getColor(lPositiveButtonColorRes)
            }
        mFragmentPositiveButton.setTextColor(lButtonTint)

        // Set the title to selected, so it scrolls correctly
        mFragmentTitleTextView.isSelected = true
    }

    /**
     * This will set all the appropriate listeners for the Views. The Views
     * must already be initialized (they are initialized in the `onCreate` method
     * by default).
     */
    private fun setViewEventListeners() {
        // For each view, you should call the handler method when your event occurs.
        // You need to define your handler method of course.

        // Positive button click
        mFragmentPositiveButton.setOnClickListener {
            // Call the handler method
            processPositiveButtonClick()
        }

        // Negative button click
        mFragmentNegativeButton.setOnClickListener {
            processNegativeButtonClick()
        }
    }

    private fun processPositiveButtonClick() {
        // First of all call the Fragment's onPositiveButtonClick method, and if we have true as
        // a result, then close this Activity.
        val lPositiveButtonClickMethodResult = mFragmentToInflate.onPositiveButtonClick()
        if (!lPositiveButtonClickMethodResult) {
            return
        }

        // Close the Activity because we have true as a result
        finishPopup()
    }

    private fun processNegativeButtonClick() {
        // This is the same as the processPositiveButtonClick, but we are managing the negative
        // button instead
        val lNegativeButtonClickMethodResult = mFragmentToInflate.onNegativeButtonClick()
        if (!lNegativeButtonClickMethodResult) {
            return
        }

        // Close the Activity using our cleanup method
        finishPopup()
    }

    private fun finishPopup() {
        // Check if we should keep the default finish behavior, in which case we finish now.
        if (SHOULD_KEEP_DEFAULT_FINISH_BEHAVIOR) {
            finish()
            return
        }

        // Then, animate the contents so they are going down
        startPopupAnimationThread(false)
    }
}