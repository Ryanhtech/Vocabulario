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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.ui.settings.SettingsActivity
import com.ryanhtech.vocabulario.utils.DataManager
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private lateinit var viewLayout: View

    var applicationContext: Context = Vocabulario.getContext()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        viewLayout = inflater.inflate(
            R.layout.fragment_profile,
            container,
            false
        )

        viewLayout.profileNameProfileFragment.text = DataManager.getName(applicationContext)
        viewLayout.settingsButtonProfile.setOnClickListener {
            startActivity(SettingsActivity::class.java)
        }
        viewLayout.editProfileButtonProfileFragment.setOnClickListener {
            startActivity(EditProfileActivity::class.java)
        }

        return viewLayout
    }

    private fun startActivity(activityClass: Class<*>) {
        /**
         * Opens an Activity.
         */

        startActivity(
            Intent(
                activity,
                activityClass
            )
        )
    }
}
