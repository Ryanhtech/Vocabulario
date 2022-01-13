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

package com.ryanhtech.vocabulario.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.ryanhtech.vocabulario.R

class SettingsCatButton constructor(context: Context, attributeSet: AttributeSet,
        defStyle: Int): ConstraintLayout(context, attributeSet, defStyle) {

    init {
        initButton(attributeSet, defStyle)
    }

    private fun initButton(attributeSet: AttributeSet, defStyle: Int) {
        inflate(context, R.layout.v_settings_cat_button, null)

        val attrs = context.obtainStyledAttributes(attributeSet,
            R.styleable.SettingsCatButton)

        try {
            val widgetTitle       = attrs.getString(R.styleable.SettingsCatButton_title)
            val iconReference     = attrs.getResourceId(R.styleable.SettingsCatButton_icon,
                                        R.drawable.ic_baseline_settings_24)
            val widgetDescription = attrs.getString(R.styleable.SettingsCatButton_description)

            val titleTextView = findViewById<TextView>(R.id.settingsCatButtonTitle)
            val descrTextView = findViewById<TextView>(R.id.settingsCatButtonDescription)
            val iconImageView = findViewById<ImageView>(R.id.settingsCatButtonIcon)
            val buttonRoot    = this

            titleTextView.text = widgetTitle
            iconImageView.setImageDrawable(AppCompatResources.getDrawable(context,
                iconReference))
            descrTextView.text = widgetDescription

            buttonRoot.background = AppCompatResources.getDrawable(context,
                R.drawable.tile_background)
            buttonRoot.setPadding(20, 20, 20, 20)
        }
        finally {
            attrs.recycle()
        }
    }

    private fun legacyInitButton(attributeSet: AttributeSet, defStyle: Int) {

    }
}