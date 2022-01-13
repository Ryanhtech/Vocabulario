/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui

import android.content.res.Resources
import com.ryanhtech.vocabulario.setup.base.UserSetupActivity
import com.ryanhtech.vocabulario.ui.settings.SettingsActivity

class ActivityRef {
    /**
     * Contains miscellaneous references for Activities
     * in this app.
     */

    companion object {
        private val activityIds: Map<Class<*>, Int> = mapOf(
            SettingsActivity::class.java to 1,
            UserSetupActivity::class.java to 2,
        )
        private val activityIdsCls: Map<Int, Class<*>> = mapOf(
            1 to SettingsActivity::class.java,
            2 to UserSetupActivity::class.java,
        )

        fun getActivityIntId(activity: Class<*>): Int? {
            /**
             * Gets the activity ID. Returns null if this ID
             * hasn't been found.
             */
            var retVal: Int? = null

            try {
                retVal = activityIds[activity]
            } catch (exception: Resources.NotFoundException) {}

            return retVal
        }

        fun getActivityIdFromInt(activity: Int): Class<*>? {
            var retVal: Class<*>? = null

            try { retVal = activityIdsCls[activity] } catch (e: Exception) {}

            return retVal
        }
    }
}