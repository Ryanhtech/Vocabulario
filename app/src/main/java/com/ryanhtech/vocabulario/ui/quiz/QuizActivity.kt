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