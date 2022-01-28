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

package com.ryanhtech.vocabulario.ui.popup

import com.ryanhtech.vocabulario.ui.popup.data.PopupDataContainer
import com.ryanhtech.vocabulario.ui.popup.data.PopupStatus

/**
 * This class contains all the information and return values of the associated
 * PopupFragment instance.
 */
class PopupInformation(popupFragment: PopupFragment) {
    /**
     * This contains the actual popupFragment instance. It is a private val.
     */
    private val mPopupFragmentInstance = popupFragment

    /**
     * This contains a PopupDataContainer instance.
     *
     * @see PopupDataContainer
     */
    private val mPopupDataContainerInstance: PopupDataContainer = TODO("Create PopupDataContainer first!")

    /**
     * This contains a PopupStatus instance.
     */
    private val mPopupStatusInstance: PopupStatus = TODO("Create PopupStatus first!")

    /**
     * This contains an unique ID associated with the PopupFragment.
     */
    private lateinit var mPopupIdentifier: String

    /**
     * Constant variable that defines the length of an unique identifier.
     */
    val POPUP_FRAGMENT_IDENTIFIER_LENGTH = 128

    /**
     * Constant variable to define the allowed characters in an identifier.
     */
    val POPUP_FRAGMENT_IDENTIFIER_ALLOWED_CHARS = ('A'..'Z') + ('a'..'z')

    // Main class constructor - this code will be executed when the class is instantiated
    init {
        // Fire the initialization method
        initLocalClassInstance()
    }

    private fun initLocalClassInstance() {
        // First generate the ID
        var lPopupIdentifier: String
        var lIsDuplicate = false
        while (true) {
            lPopupIdentifier = generateLocalIdentifier()

            // Check if there is no duplicate ID
            for (lPopupInfo in PopupInformationContainer.mPopupInfoList) {
                if (lPopupInfo.mPopupIdentifier == lPopupIdentifier) {
                    lIsDuplicate = true
                }
            }

            if (!lIsDuplicate) {
                break
            }

            // Generate a new ID if there was a duplicate
            lIsDuplicate = false
        }

        // Set the local ID to the newly created ID
        this.mPopupIdentifier = lPopupIdentifier

        //
    }

    private fun generateLocalIdentifier(): String {
        // With the allowed characters and the max length, generate the
        // random code and return it
        return (1..POPUP_FRAGMENT_IDENTIFIER_LENGTH)
            .map { POPUP_FRAGMENT_IDENTIFIER_ALLOWED_CHARS.random() }
            .joinToString("")
    }

    fun getPopupIdentifier(): String {
        return mPopupIdentifier
    }
}