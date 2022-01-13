/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import kotlinx.android.synthetic.main.activity_quiz.*

@Deprecated("Use com.ryanhtech.vocabulario.tools.quiz.ui.UserQuizActivity instead")
class QuizActivity : VocabularioActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        startTimer()
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        Thread {
            var seconds = 0
            var secondsString: String
            var minutes = 0

            while (true) {
                if (seconds >= 60) {
                    seconds = 0
                    minutes += 1
                }

                secondsString = seconds.toString()
                if (seconds < 10) secondsString = "0$secondsString"

                runOnUiThread {
                    quizTimer.text = "$minutes:$secondsString"
                }

                Thread.sleep(1000)

                seconds += 1
            }
        }.start()
    }
}