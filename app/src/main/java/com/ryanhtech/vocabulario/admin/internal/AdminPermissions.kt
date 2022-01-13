/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
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