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

package com.ryanhtech.vocabulario.ui

import android.content.res.Resources
import com.ryanhtech.vocabulario.setup.UserSetupActivity
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