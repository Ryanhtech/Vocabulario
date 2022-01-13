/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.crashhandler

import android.content.Intent
import android.os.Bundle
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.settings.FatalErrorActivity
import kotlinx.android.synthetic.main.activity_crash_handler_user_choice2.*

class CrashHandlerUserChoiceActivity : VocabularioActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_handler_user_choice2)

        closeCrashHandlerButton.setOnClickListener {
            finish()
        }

        repairAppLaunchButton.setOnClickListener {
            startActivity(Intent(this, FatalErrorActivity::class.java))
            finish()
        }
    }
}