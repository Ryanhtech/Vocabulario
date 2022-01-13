/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.setup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.utils.DataManager
import kotlinx.android.synthetic.main.fragment_reset_app_progress.view.*

class ResetAppProgressFragment : AppSetupFragment() {
    private lateinit var globalView: View

    private var secLeft = 10

    override val displayBackButton: Boolean = true
    override val displayNextButton: Boolean = false

    private var canCountdown = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        globalView = inflater.inflate(R.layout.fragment_reset_app_progress, container, false)

        // Start the countdown
        Thread {
            while (secLeft != 0) {
                Thread.sleep(1000)

                if (canCountdown) {
                    secLeft -= 1
                    requireActivity().runOnUiThread {
                        globalView.resetAppCountdown.text = secLeft.toString()

                        // Run the animation
                        globalView.resetAppCountdown.startAnimation(
                            AnimationUtils.loadAnimation(
                                activity,
                                R.anim.raise_up
                            )
                        )
                    }
                }
            }

            // RESET THE APP!!!!!
            DataManager().clearAppData(requireActivity().applicationContext)
        }.start()

        return globalView
    }

    override fun onBackPressed(): Boolean {
        if (secLeft != 0) {
            canCountdown = false
            return true
        }
        return false
    }
}