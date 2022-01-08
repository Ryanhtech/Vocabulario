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

package com.ryanhtech.vocabulario.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.system.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewLayout: View

    var collectionFragmentToGoTo: Int? = null

    companion object {
        const val FRAGMENT_COLLECTION = 1
        const val FRAGMENT_QUIZ       = 2
        const val FRAGMENT_PROFILE    = 3
    }

    var applicationContext: Context = Vocabulario.getContext()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        viewLayout = inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )

        viewLayout.nameHomeFragment.text = UiUtils().getPersonNameHello(applicationContext)

        startButtonPressListenerThread()

        return viewLayout
    }

    private fun startButtonPressListenerThread() {
        viewLayout.collectionButtonHomeFragment.setOnClickListener {
            collectionFragmentToGoTo = FRAGMENT_COLLECTION
        }

        viewLayout.quizButtonHomeFragment.setOnClickListener {
            collectionFragmentToGoTo = FRAGMENT_QUIZ
        }

        viewLayout.profileButtonHomeFragment.setOnClickListener {
            collectionFragmentToGoTo = FRAGMENT_PROFILE
        }
    }
}
