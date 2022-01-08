/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ryanhtech.vocabulario.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.util.Log
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.admin.deviceadmin.VocabularioDeviceAdminReceiver
import java.io.File

class DataManager {
    // Class that manages the data, for example, creating
    // new words to learn in the database, checking if the app
    // has already been configured, etc.

    companion object {
        // Check if the app has already been configured (= opened once)
        fun checkIfAppConfigured(applicationContext: Context): Boolean {
            // Check if the file configured.ini exists to return the result

            if (!File(applicationContext.filesDir, "configured.ini").exists()) {
                return false
            }
            return true
        }

        fun setChosenLevelInSettings(chosenLevel: Int, applicationContext: Context) {
            // Open the file and write to it
            File(
                applicationContext.filesDir,
                "userLevel.ini"
            )
                .writeText(chosenLevel.toString())

            return
        }

        fun getChosenLevel(level: String, applicationContext: Context): Int? {
            /**
             * Gets a level number. You can pass a string that you get using getString() in
             * the parameter level. It will return the following:
             *
             * "Very easy" (or equivalent):  1
             * "Easy" (or equivalent):       2
             * "Medium" (or equivalent):     3
             * "Hard" (or equivalent):       4
             * "Super hard" (or equivalent): 5
             *
             */
            val utilInstance = Utils()

            return when (level) {
                utilInstance.getStringFromOutsideActivity(R.string.level_n_very_easy, applicationContext)
                -> 1
                utilInstance.getStringFromOutsideActivity(R.string.level_n_easy, applicationContext)
                -> 2
                utilInstance.getStringFromOutsideActivity(R.string.level_n_medium, applicationContext)
                -> 3
                utilInstance.getStringFromOutsideActivity(R.string.level_n_hard, applicationContext)
                -> 4
                utilInstance.getStringFromOutsideActivity(R.string.level_n_super_hard, applicationContext)
                -> 5
                else -> null
            }
        }

        fun getChosenLevelString(level: Int, applicationContext: Context): String? {
            val utilInstance = Utils()

            return when (level) {
                1 -> utilInstance.getStringFromOutsideActivity(R.string.level_n_very_easy, applicationContext)
                2 -> utilInstance.getStringFromOutsideActivity(R.string.level_n_easy, applicationContext)
                3 -> utilInstance.getStringFromOutsideActivity(R.string.level_n_medium, applicationContext)
                4 -> utilInstance.getStringFromOutsideActivity(R.string.level_n_hard, applicationContext)
                5 -> utilInstance.getStringFromOutsideActivity(R.string.level_n_super_hard, applicationContext)
                else -> null
            }
        }

        fun markSetupAsComplete(applicationContext: Context) {
            /**
             * Marks the app as configured. It is called after the Setup.
             */

            Log.i("Setup", "Application marked as configured!")

            File(applicationContext.filesDir, "configured.ini")
                    .writeText("true")
            return
        }

        fun setName(applicationContext: Context, name: String) {
            /**
             * Changes the name of the user.
             */

            File(applicationContext.filesDir, "userName.ini")
                    .writeText(name)

            return
        }

        fun getName(applicationContext: Context): String? {
            /**
             * Gets the name of the user.
             */
            if (File(applicationContext.filesDir, "userName.ini").exists()) {
                return File(applicationContext.filesDir, "userName.ini")
                    .readText()
            }
            return null
        }
    }

    fun clearAppData(applicationContext: Context) {
        // This method DELETES the data of ¡Vocabulario! on the phone.
        // USE IT ONLY WHEN NECESSARY!!!

        // Remove admin
        try {
            VocabularioDeviceAdminReceiver.getDpm(applicationContext)
                .removeActiveAdmin(VocabularioDeviceAdminReceiver.getComponentName())
        } catch (Ignored: java.lang.Exception) { }

        try {
            VocabularioDeviceAdminReceiver.changeAdminEnabledStatus(false, applicationContext)
        } catch (Ignored: java.lang.Exception) { }

        val actMgr: ActivityManager = applicationContext.getSystemService(ACTIVITY_SERVICE)
                as ActivityManager

        // Delete the data!
        actMgr.clearApplicationUserData()

        // BAM! The data has been cleared! Now we can restart the app
        Utils().restartApp(applicationContext)
    }

    fun setOrganizationName(name: String, applicationContext: Context) {
        /**
         * Sets the organization name.
         */

        File(applicationContext.filesDir, "admorg")
            .writeText(name)
    }

    fun getOrganizationName(applicationContext: Context): String {
        return File(applicationContext.filesDir, "admorg")
            .readText()
    }

    fun isManagedByOrganization(applicationContext: Context): Boolean {
        return File(applicationContext.filesDir, "pwd.ini").exists()
    }
}