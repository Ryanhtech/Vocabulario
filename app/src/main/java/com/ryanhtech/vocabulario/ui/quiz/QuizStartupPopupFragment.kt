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

package com.ryanhtech.vocabulario.ui.quiz

import android.view.View
import android.widget.LinearLayout
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.popup.PopupFragment

class QuizStartupPopupFragment: PopupFragment() {
    // Set the layout and other properties
    override val popupLayoutRes = R.layout.fragment_quiz_startup_popup
    override val titleDefaultText = R.string.take_quiz
    override val posButtonTintRes = R.color.quiz_tint

    // Music distraction warning
    private lateinit var mMusicDistractionWarningLinearLayout: LinearLayout

    override fun popupStartJob() {
        super.popupStartJob()

        // Initialize the Views
        mMusicDistractionWarningLinearLayout = popupRootView.findViewById(
            R.id.musicDistractionQuizWarning)

        // Show the music distraction warning only if necessary
        processMusicDistractionWarning()
    }

    private fun processMusicDistractionWarning() {
        // Determine if music is running
        val lIsMusicRunning = vbUtils.vbIsMusicPlaying()

        // If we are playing music, show the warning, and hide it if it is not the case.
        var lVisibilityToApply = View.GONE
        if (lIsMusicRunning) {
            lVisibilityToApply = View.VISIBLE
        }

        // Apply the visibility to the View
        mMusicDistractionWarningLinearLayout.visibility = lVisibilityToApply
    }
}