/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.main

import android.os.Bundle
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity

class EditProfileActivity : VocabularioActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
    }
}