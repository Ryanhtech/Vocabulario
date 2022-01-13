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

package com.ryanhtech.vocabulario.ui.startup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.admin.internal.AdminPermissions
import com.ryanhtech.vocabulario.internal.block.EmergencyBlockActivity
import com.ryanhtech.vocabulario.internal.notifications.NotificationManager
import com.ryanhtech.vocabulario.internal.reset.LocalConfigurationRequest
import com.ryanhtech.vocabulario.setup.base.UserSetupActivity
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.main.MainActivity
import com.ryanhtech.vocabulario.ui.settings.FatalErrorActivity
import com.ryanhtech.vocabulario.utils.DataManager
import kotlinx.android.synthetic.main.activity_splash_screen.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : VocabularioActivity() {
    override val applyEmergencyBlock: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        setContentView(R.layout.activity_splash_screen)

        setupApp()

        // Delete the reinstall request
        LocalConfigurationRequest.deleteReConfigRequest(this)

        if (checkIfAppCrashModeEnabled()) {
            /**
             * The app crashed on the previous execution, show
             * info to the user
             */
            startActivity(
                Intent(
                    this,
                    FatalErrorActivity::class.java
                )
            )

            finish()
            return
        }

        // Check if the Emergency Block mode is enabled
        if (AdminPasswordManager.isEmergencyModeEnabled(applicationContext)) {
            // Start the activity

            startActivity(
                Intent(
                    this,
                    EmergencyBlockActivity::class.java
                )
            )

            finish()
            return
        }

        /**
         * Run the super animation if the app is being configured
         */
        if (!DataManager.checkIfAppConfigured(this)) {
            runSplashAnimation()
        } else {

            // Wait to start the new activity

            Thread {
                Thread.sleep(700)

                /**
                 * Start the main activity using an Intent, if the
                 * activity is in the foreground.
                 */

                startMainActivity()

                // Wait 50ms and exit the activity

                Thread.sleep(1000)
                finish()
                return@Thread
            }.start()
        }
    }

    private fun runSplashAnimation() {
        val animThread = Thread {
            splashImmersiveLine.isVisible = false

            val fadeInAnim = AnimationUtils.loadAnimation(
                    this, R.anim.fade_in)

            runOnUiThread {
                splashScreenAppLogo.startAnimation(fadeInAnim)
                splashScreenAppLogo.isVisible = true
            }

            Thread.sleep(1500)

            runOnUiThread {
                val pb = splashScreenLoadingProgressBar
                pb.isVisible = true
                pb.startAnimation(fadeInAnim)  // Start the same fade in animation than the logo
            }

            // Process...
            initializeAppWebServices()

            // Then, after we finished everything, start the main activity
            startMainActivity()
        }

        animThread.start()
    }

    private fun checkIfAppCrashModeEnabled(): Boolean {
        return FirebaseCrashlytics.getInstance().didCrashOnPreviousExecution()
    }

    override fun onBackPressed() {
        // Block the back button if the setup just finished

        if (parent != UserSetupActivity::class.java) {
            super.onBackPressed()
        }
    }

    private fun setupApp() {
        AdminPermissions.adminUnlocked = false

        NotificationManager.initializeNotificationChannels(this)
    }

    private fun startMainActivity() {
        if (hasWindowFocus()) {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }
    }

    private fun initializeAppWebServices() {
        // TODO: Web services initialization
        Thread.sleep(2000)
        return
    }
}