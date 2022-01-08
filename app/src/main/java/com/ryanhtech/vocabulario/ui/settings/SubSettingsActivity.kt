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