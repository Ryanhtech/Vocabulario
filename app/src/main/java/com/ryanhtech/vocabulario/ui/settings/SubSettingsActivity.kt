/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.settings

import android.os.Bundle
import android.util.Log
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.settings.subsettings.BaseSubSettingsFragment
import com.ryanhtech.vocabulario.ui.settings.subsettings.SubSettingsFragmentList

class SubSettingsActivity : VocabularioActivity() {
    companion object {
        const val EXTRA_SETTING = "subSettingsSetting"
    }

    private lateinit var currentFragment: BaseSubSettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_settings)

        val page = intent.getIntExtra(EXTRA_SETTING, 0)

        Log.d("SubSettings", "SubSettings started with page $page")

        val fragmentTemp: BaseSubSettingsFragment? = SubSettingsFragmentList
            .subSettingsFragmentsList[page]

        if (fragmentTemp == null) {
            finish()
            return
        }

        setFragment(fragmentTemp)
    }

    private fun setFragment(fragment: BaseSubSettingsFragment) {
        currentFragment = fragment

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.subSettingsFragment, fragment)
            commit()
        }
    }
}