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

package com.ryanhtech.vocabulario.ui.animations

import android.view.View
import android.view.animation.AnimationUtils
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
 * views parameter, and call `startVlaAnimation()`. This will
 * perform a VocabularioListAnimation on all of the
 * views you specified in the list.
 *
 * OPTIONAL - You can also add a delay in the `startVlaAnimation()`
 * as the delay parameter. The default delay is 100ms. For example,
 * you could use `startVlaAnimation(300)` instead of
 * `startVlaAnimation()`.
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

    fun startVlaAnimation() {
        startVlaAnimation(50)
    }

    /**
     * Starts the VocabularioListAnimation with a delay of 400ms
     * by default between each View, but this delay can be changed
     * by passing in the delay parameter your custom value. (Long)
     *
     * @param delay The delay between each View as a Long object.
     */
    fun startVlaAnimation(delay: Long) {
        // Call the internal tool
        processInternalStartAnimation(delay)
    }

    private fun processInternalStartAnimation(delay: Long) {
        // Create the new working thread that will execute all
        // the actions.
        val lWorkingThread = Thread { runWorkOnSeparateThread(delay) }

        // Execute the thread
        lWorkingThread.start()
    }

    /**
     * This hides the Views to animate.
     */
    fun hideViews() {
        mActivity.runOnUiThread {
            for (viewToHide in mViewList) viewToHide.visibility = View.INVISIBLE
        }
    }

    private fun runWorkOnSeparateThread(delay: Long) {
        // Start by hiding all the Views that we haven't animated
        hideViews()

        for (view in mViewList) {
            // For each view in the view list, perform the animation
            // and wait the delay that we have

            // Create a new SpringAnimation object
            val lSpringAnimation = SpringAnimation(view, DynamicAnimation.TRANSLATION_Y,
                view.translationY).apply {
                spring.stiffness = SpringForce.STIFFNESS_LOW
                spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
            }

            // Get an animation instance
            val lZoomInAnimationInstance = AnimationUtils.loadAnimation(mActivity, R.anim.vla_zoom_in)

            // Bring the View down a bit so we see something moving
            mActivity.runOnUiThread {
                val lViewYAxis = view.translationY
                view.translationY = (lViewYAxis - 400) * -1

                // Start the SpringAnimation on our object on the UI thread
                lSpringAnimation.start()

                // Start the zoom in animation
                view.startAnimation(lZoomInAnimationInstance)

                // Don't forget to show the View again
                if (view.visibility == View.INVISIBLE) view.visibility = View.VISIBLE
            }

            // Wait the delay we have been provided.
            Thread.sleep(delay)
        }

        // We have finished our animation. We can return
        return
    }
}