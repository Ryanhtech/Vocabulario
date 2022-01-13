/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.block

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.ui.startup.SplashScreenActivity
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import kotlinx.android.synthetic.main.activity_emergency_block.*
import kotlinx.android.synthetic.main.activity_emergency_block_wasntme.*

class EmergencyBlockActivity : VocabularioActivity() {
    override val applyEmergencyBlock = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("EmergencyBlockActivity", "intent.getStringExtra(\"nextPage\") == ${intent.getStringExtra("nextPage")}")

        if (intent.getBooleanExtra("isProgressDone", false)
            && intent.getStringExtra("nextPage") == ""
            && intent.getStringExtra("nextPage") == null) {
            setupMain()
        } else if (intent.getBooleanExtra("isProgressDone", false)
            && intent.getStringExtra("nextPage") == "wasntMe") {
            setupWasntMe()
        } else if (intent.getBooleanExtra("isProgressDone", false)
            && intent.getStringExtra("nextPage") == "") {
            setupMain()
        }
        else {
            setupProgress()
        }
    }

    override fun onBackPressed() {
        // Block the back arrow (<) on the navbar

        return
    }

    private fun setupMain() {
        setContentView(R.layout.activity_emergency_block)

        wasntMeButton.setOnClickListener {
            startActivity(Intent(this, EmergencyBlockActivity::class.java).putExtra("nextPage", "wasntMe"))
        }

        unlockButtonBlock.setOnClickListener {
            startActivity(Intent(this, UnlockAppSecurityCodeActivity::class.java))
        }
    }

    private fun setupProgress() {
        setContentView(R.layout.activity_emergency_block_progess)

        var duration: Long = 10000
        val nextPage = intent.getStringExtra("nextPage")

        if (intent.getStringExtra("nextPage") != null) {
            duration = 2000
        }

        Handler(Looper.getMainLooper()).postDelayed({

            if (hasWindowFocus()) {
                startActivity(
                    Intent(this, EmergencyBlockActivity::class.java).putExtra(
                        "isProgressDone",
                        true
                    )
                        .putExtra("nextPage", nextPage ?: "")
                )
            }

            finish()

        }, duration)
    }

    private fun setupWasntMe() {
        setContentView(R.layout.activity_emergency_block_wasntme)

        backButtonWasntMe.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        if (!AdminPasswordManager.isEmergencyModeEnabled(applicationContext)) {
            finish()

            startActivity(
                Intent(
                    this,
                    SplashScreenActivity::class.java
                )
            )
        }
    }
}