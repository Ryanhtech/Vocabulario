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

package com.ryanhtech.vocabulario.ui.animations

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.WorkerThread
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity

/**
 * This class is a representation of a Vocabulario List
 * Animation. This animation zooms and fades in a View and
 * moves it up with a SpringAnimation at the same time. It's
 * better to use it on Activity startup, and you may use
 * `overridePendingTransition(0, 0)` in your `onCreate()`
 * method to delete the activity startup transition and let
 * the user see the list animation.
 *
 * To begin, put the `Views` you need in a `List<View>`
 * instance (Kotlin). Then, create a new instance of this
 * class by passing the list you just created in the
 * views parameter, and call `startAnimation()`. This will
 * perform a VocabularioListAnimation on all of the
 * views you specified in the list.
 *
 * OPTIONAL - You can also add a delay in the `startAnimation()`
 * as the delay parameter. The default delay is 100ms. For example,
 * you could use `startAnimation(300)` instead of
 * `startAnimation()`.
 */
class VocabularioListAnimation(views: List<View>, activity: VocabularioActivity) {
    // Initialize the class with the required elements.
    init {
        internalInitClass(views, activity)
    }

    // The list of views.
    private lateinit var mViewList: List<View>

    // The working Activity.
    private lateinit var mActivity: VocabularioActivity

    private fun internalInitClass(views: List<View>, activity: VocabularioActivity) {
        // Set the m local variables to the provided parameters.
        mViewList = views
        mActivity = activity
    }

    fun startAnimation() {
        startAnimation(50)
    }

    /**
     * Starts the VocabularioListAnimation with a delay of 400ms
     * by default between each View, but this delay can be changed
     * by passing in the delay parameter your custom value. (Long)
     *
     * @param delay The delay between each View as a Long object.
     */
    fun startAnimation(delay: Long) {
        // Call the internal tool
        processInternalStartAnimation(delay)
    }

    fun startReversedAnimation() {
        startReversedAnimation(50)
    }

    /**
     * Starts the reversed animation. The widgets will go down
     * and will fade in at the same time.
     */
    fun startReversedAnimation(delay: Long) {
        processInternalStartReversedAnimation(delay)
    }

    private fun processInternalStartAnimation(delay: Long) {
        // Create the new working thread that will execute all
        // the actions.
        val lWorkingThread = Thread { runAnimationWorkOnSeparateThread(delay) }

        // Execute the thread
        lWorkingThread.start()
    }

    private fun processInternalStartReversedAnimation(delay: Long) {
        // Create the working thread for the reversed animation
        val lReverseWorkingThread = Thread { runReversedAnimationWorkOnSeparateThread(delay) }

        // Start the thread
        lReverseWorkingThread.start()
    }

    /**
     * This hides the Views to animate.
     */
    fun hideViews() {
        mActivity.runOnUiThread {
            for (viewToHide in mViewList) viewToHide.visibility = View.INVISIBLE
        }
    }

    @WorkerThread
    private fun runAnimationWorkOnSeparateThread(delay: Long) {
        runAnyAnimationWorkOnSeparateThread(delay, false)
    }

    @WorkerThread
    private fun runReversedAnimationWorkOnSeparateThread(delay: Long) {
        runAnyAnimationWorkOnSeparateThread(delay, true)
    }

    @WorkerThread
    private fun runAnyAnimationWorkOnSeparateThread(delay: Long, isReversedAnimation: Boolean) {
        // Start by hiding all the Views that we haven't animated
        hideViews()

        // If we must make a reversed animation, reverse the list!!
        val lViewList = mViewList
        if (isReversedAnimation) {
            lViewList.reversed()
        }

        // Ignore the delay if we are in a reversed animation
        var lDelay = delay
        if (isReversedAnimation) {
            lDelay = 0
        }

        // To perform the SpringAnimation, we need a spring stiffness.
        // We may prefer a low stiffness on a standard animation, and
        // an average one on a reversed animation.
        var lSpringStiffness = SpringForce.STIFFNESS_LOW
        if (isReversedAnimation) {
            lSpringStiffness = SpringForce.STIFFNESS_MEDIUM
        }

        for (view in lViewList) {
            // For each view in the view list, perform the animation
            // and wait the delay that we have

            // Create a new SpringAnimation object
            val lSpringAnimation = SpringAnimation(view, DynamicAnimation.TRANSLATION_Y,
                view.translationY).apply {
                spring.stiffness = lSpringStiffness
                spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
            }

            // Get an animation instance, depending on the requested mode
            @AnimRes val lAnimationRes = if (isReversedAnimation) R.anim.no_anim
                else R.anim.vla_zoom_in

            val lZoomInAnimationInstance = AnimationUtils.loadAnimation(mActivity, lAnimationRes)

            // Bring the View down a bit (or up) so we see something moving
            mActivity.runOnUiThread {
                val lViewYAxis = view.translationY
                val lDestinationTranslationOnYAxis =
                    if (isReversedAnimation) {
                        lViewYAxis - 200
                    }
                    else { (lViewYAxis - 400) * -1 }

                view.translationY = lDestinationTranslationOnYAxis

                // Start the SpringAnimation on our object on the UI thread
                lSpringAnimation.start()

                // Start the zoom (in or out) animation
                view.startAnimation(lZoomInAnimationInstance)

                // Don't forget to show the View again
                if (view.visibility == View.INVISIBLE) view.visibility = View.VISIBLE
            }

            // Wait the delay we have been provided.
            Thread.sleep(lDelay)
        }

        // We have finished our animation. We can return
        return
    }
}