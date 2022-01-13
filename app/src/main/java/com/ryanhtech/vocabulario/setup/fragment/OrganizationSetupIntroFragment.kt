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
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.config.UserSetupList

class OrganizationSetupIntroFragment : AppSetupFragment() {

    private lateinit var globalView: View
    override val nextStep: Int = UserSetupList.ORG_SETUP_ENTER_NAME

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        globalView = inflater.inflate(R.layout.fragment_organization_setup_intro, container, false)

        return globalView
    }

    override fun onNextPressed(): Boolean {
        return true
    }
}