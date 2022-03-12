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

package com.ryanhtech.vocabulario.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.ui.fragment.VocabularioFragment
import com.ryanhtech.vocabulario.ui.popup.PopupFragmentExecutor
import com.ryanhtech.vocabulario.ui.quiz.QuizStartupPopupFragment
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_quiz.view.*

class QuizFragment: VocabularioFragment() {

    private lateinit var viewLayout: View
    var applicationContext: Context = Vocabulario.getContext()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        viewLayout = inflater.inflate(
            R.layout.fragment_quiz,
            container,
            false
        )

        viewLayout.startQuizButton.setOnClickListener {
            // Show the popup
            PopupFragmentExecutor.displayPopupFragment(QuizStartupPopupFragment(),
                requireActivity())
        }

        Handler(Looper.getMainLooper()).postDelayed({
                viewLayout.quizPresentation.text = applicationContext.getString(R.string.quiz_presentation)
                UiUtils.animateViewIncorrectValue(viewLayout.quizPresentation, intensity = 2)
            },1000
        )

        return viewLayout
    }
}