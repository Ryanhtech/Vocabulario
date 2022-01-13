/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.setup.base

import androidx.fragment.app.Fragment

open class AppSetupFragment : Fragment() {
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
}