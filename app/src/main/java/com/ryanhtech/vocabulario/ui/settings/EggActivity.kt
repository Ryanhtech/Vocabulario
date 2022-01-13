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

package com.ryanhtech.vocabulario.ui.settings

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.utils.Utils

class EggActivity : VocabularioActivity() {
    private lateinit var regenerateButton:     Button
    private lateinit var numberTextView:       TextView
    private lateinit var instructionsTextView: TextView

    private lateinit var generateThread: Thread

    private val numberRange = (1..6)

    private var animationFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_egg)

        regenerateButton = findViewById(R.id.numberRegenerateEgg)
        numberTextView = findViewById(R.id.numberTextViewEgg)
        instructionsTextView = findViewById(R.id.instructionsEgg)

        animateWidgets()

        generateThread = Thread {}

        regenerateButton.setOnClickListener {
            regenerateNumber()
        }
    }

    private fun regenerateNumber() {
        regenerateButton.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.zoom_out
            )
        )

        var delay: Long = 30
        var oldNumber = 0
        var currentNumber = 1

        regenerateButton.isVisible = false
        regenerateButton.isEnabled = false

        generateThread = Thread {
            while (delay < 300) {
                while (currentNumber == oldNumber) {
                    currentNumber = numberRange.random()
                }

                oldNumber = currentNumber

                runOnUiThread {
                    numberTextView.text = currentNumber.toString()
                }

                Utils().vibratePhone(25, applicationContext)

                delay += delay / 16
                Thread.sleep(delay)
            }

            runOnUiThread {
                numberTextView.isVisible = true

                Handler(Looper.getMainLooper()).postDelayed({
                    regenerateButton.isVisible = true
                    regenerateButton.isEnabled = true

                    regenerateButton.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.zoom_in
                        )
                    )

                }, 500
                )
            }
        }

        generateThread.start()
    }

    override fun onStop() {
        super.onStop()
        overridePendingTransition(
            R.anim.zoom_out,
            R.anim.zoom_out
        )
    }

    private fun animateWidgets() {
        Thread {
            Thread.sleep(2000)

            runOnUiThread {
                instructionsTextView.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.fade_left
                    )
                )
            }
            Thread.sleep(500)

            runOnUiThread {
                instructionsTextView.isVisible = false
            }

            Thread.sleep(1000)

            val widgetsAnimation = AnimationUtils.loadAnimation(
                this,
                R.anim.fade_in
            )

            widgetsAnimation.duration = 500

            runOnUiThread {

                numberTextView.isVisible = true
                regenerateButton.isVisible = true

                regenerateButton.startAnimation(widgetsAnimation)
                numberTextView.startAnimation(widgetsAnimation)
            }

            Thread.sleep(500)

            runOnUiThread {

                regenerateButton.isEnabled = true
            }

            animationFinished = true

        }.start()
    }
}