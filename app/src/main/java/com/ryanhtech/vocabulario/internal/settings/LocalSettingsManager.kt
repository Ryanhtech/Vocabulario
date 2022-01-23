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
import com.ryanhtech.vocabulario.BuildConfig
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import java.util.*

/**
 * This class manages the settings stored locally. It manages
 * the username, the supervision status, the administrator
 * password, and many more.
 *
 * @param context The context to use while processing the
 * operations.
 * @since Initial version
 * @author Ryanhtech Labs
 */
class LocalSettingsManager(context: Context) {
    // Store the shared preferences key inside an object so it's static.
    companion object {
        /**
         * Vocabulario Shared Preferences settings key
         *
         * Congratulations! You have found the key to the Vocabulario
         * user settings!
         * This key will allow you to create, edit, read and delete
         * Vocabulario settings. It is useful for Vocabulario system
         * modules, such as the UserSettings class, or the setup
         * classes.
         *
         * @author Ryanhtech Labs
         * @since Initial version
         */
        const val VOCABULARIO_USER_SETTINGS_KEY = BuildConfig.APPLICATION_ID +
                ".internal.settings.VOCABULARIO_USER_SETTINGS"

        /**
         * This is the key to the UserSettings JSON string stored into
         * the settings key. It is used to get the configuration and
         * to write to it.
         *
         * @since Initial version
         * @author Ryanhtech Labs
         * @see VOCABULARIO_USER_SETTINGS_KEY
         */
        const val VOCABULARIO_USER_SETTINGS_VALUE_CONFIG = "$VOCABULARIO_USER_SETTINGS_KEY.CFG"
    }

    // Store the local context here directly when the class is instantiated.
    private val mContext = context

    /**
     * This method will write a UserSettings instance into the settings.
     * Use it to change a setting, or to create them.
     *
     * It uses the Vocabulario Shared Preferences settings key and the
     * VOCABULARIO_USER_SETTINGS_VALUE_CONFIG constant to store this data.
     *
     * @author Ryanhtech Labs
     * @since Initial version
     * @see UserSettings
     */
    fun applyUserSettingsInstance(instance: UserSettings) {
        // First of all, get the shared preferences editor instance
        val lSharedPrefsEditorInst = mContext.getSharedPreferences(VOCABULARIO_USER_SETTINGS_KEY,
            Context.MODE_PRIVATE).edit()

        // Then convert the UserSettings instance to a JSON format
        val lSettingsJson = convertUserSettingsToJsonInternal(instance)

        // Save thin to the shared preferences
        lSharedPrefsEditorInst.putString(VOCABULARIO_USER_SETTINGS_VALUE_CONFIG, lSettingsJson)

        // Commit our changes. Throw an exception if this wasn't successful
        if (!lSharedPrefsEditorInst.commit()) {
            // Throw a RuntimeException because we don't know why it failed
            throw RuntimeException("The write to shared preferences failed")
        }
    }

    private fun convertUserSettingsToJsonInternal(settings: UserSettings): String {
        // Load a new GSON instance
        val lGson = Vocabulario.getGson()

        // Convert our instance to a String and return the result
        return lGson.toJson(settings)
    }

    /**
     * Returns a UserSettings instance corresponding to the settings stored
     * in the Shared Preferences. It converts the JSON to an UserSettings
     * instance.
     *
     * @since Initial version
     * @author Ryanhtech Labs
     */
    fun getUserSettingsInstance(): UserSettings? {
        // Get the shared preferences
        val lSharedPrefs = mContext.getSharedPreferences(VOCABULARIO_USER_SETTINGS_KEY,
            Context.MODE_PRIVATE)

        // Extract the JSON settings from the preferences
        val lSettingsJson = lSharedPrefs.getString(VOCABULARIO_USER_SETTINGS_VALUE_CONFIG,
            "noSettings")

        // If we haven't got any settings return null
        if (lSettingsJson == null) {
            return lSettingsJson  // <-- lSettingsJson is obviously null
        }

        // Now convert the JSON into a UserSettings instance and return it
        return convertJsonToUserSettingsInternal(lSettingsJson)
    }

    private fun convertJsonToUserSettingsInternal(jsonContent: String): UserSettings {
        // Load a GSON instance
        val lGsonInst = Vocabulario.getGson()

        // Create the UserSettings instance from the JSON that has been passed
        val lUserSettingsInst: UserSettings
        try {
            lUserSettingsInst = lGsonInst.fromJson(jsonContent, UserSettings::class.java)
                    as UserSettings
        }
        catch (err: Exception) {
            // An exception has occurred while converting the string to a UserSettings
            // instance. The settings might be corrupted.
            throw InvalidPropertiesFormatException("Error while creating user settings instance " +
                "from JSON: the settings might be corrupted!")
        }

        return lUserSettingsInst
    }
}