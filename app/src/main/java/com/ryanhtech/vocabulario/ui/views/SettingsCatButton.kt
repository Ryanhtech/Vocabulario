/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
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