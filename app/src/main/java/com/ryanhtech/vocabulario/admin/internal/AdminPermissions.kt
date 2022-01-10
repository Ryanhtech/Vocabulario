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

package com.ryanhtech.vocabulario.admin.internal

import android.content.Context
import android.content.Intent
import com.ryanhtech.vocabulario.admin.ui.AdminPassActivity
import com.ryanhtech.vocabulario.admin.ui.AdminSupervisionInfoActivity

class AdminPermissions {
    companion object {
        var adminUnlocked = false
            set(value) {
                //if (!authAdminUnlock && value) throw SecurityException("Admin unlocking denied!")
                if (!value) authAdminUnlock = false

                field = value
            }

        private var authAdminUnlock: Boolean = false

        fun displaySupervisionInfo(context: Context) {
            context.startActivity(
                Intent(context,
                    AdminSupervisionInfoActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    fun startProtectedActivity(context: Context) {
        /**
         * Prompt the user to enter the administrator password
         * before unlocking the protected Activity.
         *
         * @param context The context of the Activity.
         */

        authAdminUnlock = true

        context.startActivity(
            Intent(
                context,
                AdminPassActivity::class.java
            )
        )
    }
}