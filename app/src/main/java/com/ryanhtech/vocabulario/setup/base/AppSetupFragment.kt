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

package com.ryanhtech.vocabulario.setup.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.fragment.VocabularioFragment

open class AppSetupFragment : VocabularioFragment() {
    /**
     * Base Fragment class that is used during the Setup.
     */

    open val nextStep: Int = 2

    /**
     * If the Next button must be displayed.
     */

    open val displayNextButton = true

    /**
     * If the Back button must be displayed.
     */

    open val displayBackButton = true

    /**
     * The layout to inflate when you start up the Fragment. If `null`,
     * no layout will be shown.
     */
    @LayoutRes open val fragmentLayout: Int? = null

    /**
     * The View of the fragment.
     */
    open var globalView: View? = null

    /**
     * If you should display the standard Setup items. Setting this to `false` will remove all
     * [View]s on screen, and your content will be the only thing visible.
     */
    open val displaySetupItems = true

    /**
     * The title resource for the fragment.
     */
    @StringRes open val fragmentTitleResource: Int = R.string.setup_finished_title

    /**
     * The icon to display to the user while the fragment is on the screen.
     */
    @DrawableRes open val fragmentIconResource : Int = R.drawable.ic_baseline_settings_24

    /**
     * The description to show to the user. If `null`, no description will be
     * displayed. **Do not** use `0`.
     */
    @StringRes open val fragmentDescriptionResource: Int? = null

    open fun onNextPressed(): Boolean {
        /**
         * When the Next button in the UserSetupActivity is pressed.
         * @return true if the Setup must go forward to the next step.
         */
        return true
    }

    open fun onBackPressed(): Boolean {
        /**
         * When the Back button in the UserSetupActivity, or the Back
         * button in the navigation bar is pressed.
         * @return true if the setup must go to back.
         */
        return true
    }

    open fun startJob() {
        /**
         * When the fragment is ready.
         */
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the view, and return the view. After this operation, the
        // fragment can access views with globalView.<viewName>.
        val lFragmentLayout = fragmentLayout
            ?: return super.onCreateView(inflater, container, savedInstanceState)

        globalView = inflater.inflate(lFragmentLayout, container, false)

        // Start the job to process the Fragment's job
        this.startJob()

        return globalView
    }
}