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

package com.ryanhtech.vocabulario.internal.settings

import android.content.Context
import com.ryanhtech.vocabulario.security.Hash
import com.ryanhtech.vocabulario.utils.DataManager
import java.util.*

/**
 * This class contains all the user settings related to
 * personal data, like the user name. It logs many things,
 * like the setup information (e.g. the date and time the
 * setup finished).
 *
 * @author Ryanhtech Labs
 * @since Initial Version
 */
class UserSettings(context: Context) {
    // The username of the user.
    private var mUsername = ""

    // The install time/date
    private var mInstDate = Calendar.getInstance()

    // The supervision type to apply
    private var mSupervisionType = VocabularioSupervisionType.SUPERVISION_TYPE_NONE

    // The administrator password (stored as a SHA-512 hashcode)
    private var mAdminPassword = ""

    // The security code (stored as a SHA-512 hashcode too)
    private var mSecurityCode = ""

    // The school name
    private var mSchoolName = ""

    // The local context.
    private val mContext = context

    /**
     * This sets the username of the user inside the UserSettings instance.
     * The name you set must follow the following rules:
     *   - Your name must not be shorter than 2 letters.
     *   - Your name must begin with an uppercase letter and have lowercase
     *     letters all along.
     *   - Your name length must not exceed 24 letters.
     *   - Your name must not start with a whitespace or finish with a whitespace.
     *
     * If your name matches with the following rules, you can call this method.
     * You can check if it matches using the `checkIsNameValid(username: String)`.
     */
    fun setUsername(username: String) {
        // Check if the name is valid
        val lCheckStatus = checkIsNameValid(username)
        if (lCheckStatus != NameCheckStatus.NAME_VALID) {
            throw IllegalStateException("The name is not valid! ($lCheckStatus)")
        }

        // Set the username after all the checks
        mUsername = username
    }

    /**
     * This method checks if the provided username is valid, and matches
     * with the rules for usernames in Vocabulario. It returns the following
     * values:
     *
     *  - `NameCheckStatus.NAME_VALID`: if the name is valid. Good job!
     *  - `NameCheckStatus.NAME_WHITESPACE`: the name doesn't respect the whitespace
     *    rules.
     *  - `NameCheckStatus.NAME_CASE_ERROR`: the name doesn't respect the case rules.
     *  - `NameCheckStatus.NAME_OUT_OF_INDEX`: the name is too short or too long.
     */
    fun checkIsNameValid(username: String): String {
        // Determine if the name is valid step by step
        // Step 1: The name must not start by a whitespace
        // or finish by a whitespace
        val isStartingWithWhitespace = username.startsWith(" ")
        val isFinishingWithWhitespace = username.endsWith(" ")

        if (isStartingWithWhitespace || isFinishingWithWhitespace) {
            return NameCheckStatus.NAME_WHITESPACE
        }

        // Step 2: The name must not exceed 24 letters, and must not
        // be shorter that 2 letters
        if (username.length < 2 || username.length > 24) {
            return NameCheckStatus.NAME_OUT_OF_INDEX
        }

        // Step 3: The name must start by an uppercase letter and
        // continue with lowercase letters
        if (username.first() != username.first().uppercaseChar()) {
            // If the original first letter != to the
            // same letter but uppercase it means that
            // our letter is not uppercase
            return NameCheckStatus.NAME_CASE_ERROR
        }

        // Make a for loop to loop in all the letters
        for ((workingIndex, letter) in username.toCharArray().withIndex()) {
            // Of course skip the first letter
            if (workingIndex > 0) {
                if (letter != username[workingIndex].uppercaseChar()) {
                    // If the letter is not uppercase
                    return NameCheckStatus.NAME_CASE_ERROR
                }
            }
            // The index will increment automatically
        }

        return NameCheckStatus.NAME_VALID
    }

    /**
     * This method updates the date in the class. You can't provide
     * a custom date, it will generate it automatically!
     */
    fun updateDate() {
        // This operation is quite simple: override the current date
        // by the generated one
        mInstDate = Calendar.getInstance()
    }

    /**
     * Sets the supervision status in the class. CAUTION: You can't
     * change this if the app has finished setup!
     */
    fun setSupervised(isSupervised: Boolean) {
        if (DataManager.checkIfAppConfigured(mContext)) {
            throw SecurityException("You can't call this method if the app is configured!")
        }

        // TODO: Change the supervision status
    }

    /**
     * This sets the administrator password.
     */
    fun setAdministratorPassword(password: String) {
        // Hash the password and set it into the instance
        mAdminPassword = Hash.getHash(password)
    }

    /**
     * This sets the security code.
     */
    fun setSecurityCode(secCode: String) {
        // Hash the code and save it locally
        mSecurityCode = Hash.getHash(secCode)
    }

    /**
     * This sets the school name.
     */
    fun setSchoolName(schoolName: String) {
        mSchoolName = schoolName
    }
}