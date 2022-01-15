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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.ryanhtech.vocabulario.R

/**
 * This [BroadcastReceiver] receives the local Collection broadcasts
 * (broadcasts that are related to the Collection in the app, that are
 * not shared with other apps). It takes the appropriate action depending
 * on the received broadcast and its content.
 *
 * @since initial version
 * @author Ryanhtech Labs
 */
class CollectionLocalReceiver : BroadcastReceiver() {
    // Define the constants inside an object
    companion object {
        /**
         * The action that broadcasts when the user wants to add
         * a new word group in its Collection. It is a string ID
         * that points to the action name as a String.
         *
         * @since initial version
         * @author Ryanhtech Labs
         */
        @StringRes const val ACTION_SHOW_ADD_WORD_GROUP_UI =
            R.string.action_collection_add_word_group
    }

    override fun onReceive(context: Context, intent: Intent) {
        // We have received a broadcast! Let's check this out!
        // First get the strings that represents the broadcasts
        // because we can't do anything without them honestly
        val lActionShowAddWordGroupUI = context.getString(ACTION_SHOW_ADD_WORD_GROUP_UI)

        // We have initialized all our strings, so check which string do we
        // talk about.
        val lActionFromIntent = intent.action

        if (lActionFromIntent == lActionShowAddWordGroupUI) {
            // The user wants to add a new word group
            ReceiverActionLayer.startAddWordGroupUiActivity(context)
            return
        }

        // The action is invalid
        throw IllegalArgumentException("Can't resolve action packed into the intent")
    }
}