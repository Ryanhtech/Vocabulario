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

package com.ryanhtech.vocabulario.system.block

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.AdminPasswordManager
import com.ryanhtech.vocabulario.system.SplashScreenActivity
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