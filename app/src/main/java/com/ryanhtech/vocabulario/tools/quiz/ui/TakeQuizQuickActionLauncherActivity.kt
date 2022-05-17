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

package com.ryanhtech.vocabulario.tools.quiz.ui

import android.os.Bundle
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.quiz.QuizStartupPopupFragment

/**
 * This Activity is opened from a launcher shortcut and starts the [QuizStartupPopupFragment].
 */
class TakeQuizQuickActionLauncherActivity : VocabularioActivity() {
    /**
     * This val contains an instance of [QuizStartupPopupFragment]. This instance will be used while
     * [TakeQuizQuickActionLauncherActivity] starts the Popup contained in the instance.
     */
    private val mPopupInstance = QuizStartupPopupFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Delete the startup transition
        overridePendingTransition(0, 0)

        // Start the popup fragment when the app has started
        startPopupFragment()
    }

    override fun onPause() {
        super.onPause()

        // When the user leaves the activity, or if an Activity has started over this Activity,
        // finish the current Activity
        finish()
    }

    private fun startPopupFragment() {
        // Start the popup fragment using the built-in VocabularioActivity API
        displayPopupFragment(mPopupInstance, this)
    }
}