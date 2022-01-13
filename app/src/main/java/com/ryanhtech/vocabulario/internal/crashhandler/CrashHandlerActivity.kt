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
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity

class CrashHandlerActivity : VocabularioActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_handler)
        checkIntegrity()
    }

    private fun checkIntegrity() {
        Thread {
            // Send unsent error reports
            FirebaseCrashlytics.getInstance().sendUnsentReports()

            // Wait 5 seconds
            Thread.sleep(5000)

            startActivity(Intent(this, CrashHandlerUserChoiceActivity::class.java))
            finish()
        }.start()
    }
}