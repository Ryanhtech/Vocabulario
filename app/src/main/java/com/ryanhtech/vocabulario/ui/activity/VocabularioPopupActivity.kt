/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.activity

import android.os.Bundle
import com.ryanhtech.vocabulario.R

open class VocabularioPopupActivity : VocabularioActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_bottom_transition,
            R.anim.fade_out_popup_back_transition)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in_popup_back_transition,
            R.anim.slide_out_bottom_transition)
    }
}