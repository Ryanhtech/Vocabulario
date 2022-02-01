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
    private lateinit var mPopupDataContainerInstance: PopupDataContainer

    /**
     * This contains a PopupStatus instance.
     */
    private lateinit var mPopupStatusInstance: PopupStatus

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

    /**
     * The popup status. Use this to change the status of the Popup.
     *
     * **WARNING**: This variable will NEVER change, even when you assign a value
     * to it (e.g. `myPopupInfo.popupStatus = this.bla`). Instead, it will verify
     * your value and set it into a private variable. To say this shortly, when you
     * reference this variable (`myPopupInfo.popupStatus`), it will return the value
     * of mPopupStatusInstance instead, and when you set it, it will set the
     * mPopupStatusInstance variable to your request.
     *
     * @since Initial version
     */
    var popupStatus: PopupStatus
    get() {
        return mPopupStatusInstance
    }
    set(value) {
        mPopupStatusInstance = value
    }

    /**
     * The Popup ID. You shouldn't use it in your program.
     *
     * **WARNING**: Do not set this value. If you do, you will have an
     * `IllegalAccessException` right in the face.
     */
    var popupId: String
    get() {
        return mPopupIdentifier
    }
    set(value) {
        // Fun fact: I used the $value in the string because I didn't want Android Studio
        // telling me "Variable is never used". I also used it because it's quite useful
        // for debugging, when you don't read the docs just above. ^^
        //                                                         ||
        throw IllegalAccessException("Don't set this value!! (your value: $value)")
    }

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

        // Initialize a new PopupStatus instance and set it
        val lNewPopupStatusInstance = PopupStatus()
        this.mPopupStatusInstance = lNewPopupStatusInstance

        // Init a new PopupDataContainer instance
        // TODO PopupDataContainer
        val lPpDataContainer = PopupDataContainer()
        mPopupDataContainerInstance = lPpDataContainer
    }

    private fun generateLocalIdentifier(): String {
        // With the allowed characters and the max length, generate the
        // random code and return it
        return (1..POPUP_FRAGMENT_IDENTIFIER_LENGTH)
            .map { POPUP_FRAGMENT_IDENTIFIER_ALLOWED_CHARS.random() }
            .joinToString("")
    }
}