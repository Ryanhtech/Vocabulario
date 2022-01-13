/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.ui.quiz.QuizActivity
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_quiz.view.*

class QuizFragment: Fragment() {

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
            requireActivity().startActivity(
                Intent(
                    activity,
                    QuizActivity::class.java
                )
            )
        }

        Handler(Looper.getMainLooper()).postDelayed({
                viewLayout.quizPresentation.text = applicationContext.getString(R.string.quiz_presentation)
                UiUtils.animateViewIncorrectValue(viewLayout.quizPresentation, intensity = 2)
            },1000
        )

        return viewLayout
    }
}