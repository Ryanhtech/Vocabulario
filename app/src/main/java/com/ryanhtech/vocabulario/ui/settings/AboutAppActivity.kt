/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.ryanhtech.vocabulario.BuildConfig
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.utils.EASTEREGG_PUSH_TARGET
import com.ryanhtech.vocabulario.utils.EASTEREGG_STPRSD_TARGET
import com.ryanhtech.vocabulario.utils.Utils
import kotlin.properties.Delegates


class AboutAppActivity : VocabularioActivity() {
    // Easter egg stuff
    private var eggPushCount by Delegates.notNull<Int>()
    private var eggStayPressedCount by Delegates.notNull<Int>()

    // Widgets
    private lateinit var versionNameButton: Button
    private lateinit var versionCodeButton: Button
    private lateinit var versionTypeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about_app)

        // Initialize the widgets
        versionNameButton = findViewById(R.id.versionNameButtonAbout)
        versionCodeButton = findViewById(R.id.versionCodeButtonAbout)
        versionTypeButton = findViewById(R.id.buildTypeButtonAbout)

        configureInfoOnScreen()

        resetEgg()

        // Set the onClickListeners (for the easter egg!)
        versionNameButton.setOnLongClickListener {
            if (eggStayPressedCount < EASTEREGG_STPRSD_TARGET) {
                eggStayPressedCount += 1

                // Vibrate the phone
                Utils().vibratePhone(400, applicationContext)

                // Check if the easter egg can be run
                if (checkEasterEgg()) runEasterEgg()
            }

            return@setOnLongClickListener true
        }

        versionCodeButton.setOnClickListener {
            if (eggPushCount < EASTEREGG_PUSH_TARGET) {
                eggPushCount += 1

                // Vibrate the phone
                Utils().vibratePhone(25, applicationContext)

                if (checkEasterEgg()) runEasterEgg()
            }
        }
    }

    private fun runEasterEgg() {
        startActivity(
            Intent(
                this,
                EggActivity::class.java
            )
        )

        overridePendingTransition(
            R.anim.zoom_in,
            0
        )
    }

    private fun checkEasterEgg(): Boolean {
        return eggPushCount == EASTEREGG_PUSH_TARGET
                && eggStayPressedCount == EASTEREGG_STPRSD_TARGET
    }

    @SuppressLint("SetTextI18n")
    private fun configureInfoOnScreen() {
        /**
         * This function sets the information about the app in the buttons.
         */

        versionNameButton.text = "${getString(R.string.v_name)} ${BuildConfig.VERSION_NAME}"
        versionCodeButton.text = "${getString(R.string.v_code)} ${BuildConfig.VERSION_CODE}"

        /**
         * If the build is a debug build.
         */
        if (BuildConfig.DEBUG) {
            versionTypeButton.text = getString(R.string.debug_build)
        }
        else {
            versionTypeButton.text = getString(R.string.stable_version)
            versionTypeButton.setTextColor(getColor(R.color.green))
        }
    }

    private fun resetEgg() {
        // Initialize the easter egg variables
        eggPushCount = 0
        eggStayPressedCount = 0

        return
    }
}