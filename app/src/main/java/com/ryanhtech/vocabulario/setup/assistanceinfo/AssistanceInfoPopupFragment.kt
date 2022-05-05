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

package com.ryanhtech.vocabulario.setup.assistanceinfo

import android.widget.TextView
import com.google.firebase.installations.FirebaseInstallations
import com.ryanhtech.vocabulario.BuildConfig
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.popup.PopupFragment
import com.ryanhtech.vocabulario.utils.Utils

class AssistanceInfoPopupFragment : PopupFragment() {
    // Define the PopupFragment's properties
    override val titleDefaultText = R.string.assistance_info
    override val hasNegativeButton = false
    override val posButtonTintRes = R.color.primary_colour
    override val posButtonDefaultText = android.R.string.ok
    override val popupLayoutRes = R.layout.fragment_assistance_info_popup

    /**
     * [TextView] that contains the version name.
     */
    private lateinit var mVersionNameTextView: TextView

    /**
     * [TextView] that contains the version code.
     */
    private lateinit var mVersionCodeTextView: TextView

    override fun popupStartJob() {
        super.popupStartJob()

        // Set the required information on the screen so the user can see this info
        initViews()
        initializeUiInformation()
    }

    private fun initializeUiInformation() {
        // First initialize the info, such as the version name
        val lVersionName = BuildConfig.VERSION_NAME
        val lVersionCode = BuildConfig.VERSION_CODE
        val lFirebaseId  = FirebaseInstallations.getInstance().id
        val lDeviceId    = Utils.getDeviceId(applicationContext)

        // Set the version name and the version code
        mVersionNameTextView.text = lVersionName
        mVersionCodeTextView.text = lVersionCode.toString()
    }

    private fun initViews() {
        // Initialize the views using findViewById calls on the Popup's root View
        mVersionNameTextView = popupRootView.findViewById(R.id.assistanceInfoVersionNameIndicator)
        mVersionCodeTextView = popupRootView.findViewById(R.id.assistanceInfoVersionCodeIndicator)
    }
}