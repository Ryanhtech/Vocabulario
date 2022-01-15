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

package com.ryanhtech.vocabulario.internal.receiver

import android.content.Context
import android.content.Intent
import com.ryanhtech.vocabulario.BuildConfig

/**
 * This class contains all the actions receivers inside
 * Vocabulario can do.
 *
 * @see CollectionLocalReceiver
 * @author Ryanhtech Labs
 */
object ReceiverActionLayer {
    /**
     * This starts the "Add Word Group" activity.
     */
    fun startAddWordGroupUiActivity(context: Context) {
        // Create the Intent to point to the new Activity
        val lCreateWgUiIntent = Intent()

        // We can't use the normal Intent initialization method because
        // we aren't in an Activity! Set the class name and flags instead
        lCreateWgUiIntent.setClassName(BuildConfig.APPLICATION_ID,
            BuildConfig.APPLICATION_ID + ".tools.collection.ui.CreateWordGroupActivity")

        // Put the new task flag so the Activity is started in a new
        // Activity stack
        lCreateWgUiIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Finally start the Activity
        context.startActivity(lCreateWgUiIntent)
    }
}