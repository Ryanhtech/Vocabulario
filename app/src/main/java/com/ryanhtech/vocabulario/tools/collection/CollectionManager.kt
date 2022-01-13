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

package com.ryanhtech.vocabulario.tools.collection

import android.content.Context
import android.content.Intent
import android.util.Log
import com.ryanhtech.vocabulario.BuildConfig
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.tools.collection.db.CollectionData
import com.ryanhtech.vocabulario.tools.collection.db.CollectionDatabase
import java.io.File

class CollectionManager(collectionDir: String) {
    companion object {
        // WARNING: DO NOT MODIFY THE VALUES BELOW OR APP UPDATES
        // COULD BE UNUSABLE!!! IF YOU WANT TO CHANGE THEM, JUST
        // FORGET YOUR IDEA!
        private const val COLLECTION_BASE_FILE_NAME = "collection_base.collection"
        private const val COLLECTION_SPAN_FILE_NAME = "collection_sp.collection"  // SPAN = Spanish
        private const val COLLECTION_META_FILE_NAME = "collection_meta.collection"
        private const val COLLECTION_DATA_DIR_NAME  = "/collection"  // To be used with context.filesDir

        private val TAG = this::class.java.simpleName

        fun setup(context: Context) {
            // Log the event in Logcat
            Log.i(TAG, "Collection setting up...")

            // In the argument below, in getCollectionDir(), if the result is null, it means
            // that the collection dir is missing, so pass "" to isCollectionInitialized so
            // that the result will always be false in this case.
            if (isCollectionInitialized(getCollectionDir(context) ?: "")) {
                throw IllegalStateException("The Collection is already configured")
            }

            // Create the Collection directory if it doesn't exists
            val collectionDirectory = File("${context.filesDir}/collection")
            if (!collectionDirectory.exists()) {
                collectionDirectory.mkdirs()
            }

            // get the Collection dir to use it to save the new files
            val collectionDir = getCollectionDir(context)

            processCreateCollection(collectionDir!!)
        }

        private fun getCollectionDir(context: Context): String? {
            val pathname = "${context.filesDir}${COLLECTION_DATA_DIR_NAME}"

            return if (File(pathname).exists()) pathname else null
        }

        private fun processCreateCollection(collectionDir: String) {
            // Create new files
            File(collectionDir, COLLECTION_BASE_FILE_NAME).createNewFile()
            File(collectionDir, COLLECTION_SPAN_FILE_NAME).createNewFile()
            File(collectionDir, COLLECTION_META_FILE_NAME).createNewFile()  // Stores the metadata about the words
        }

        /**
         * Checks if the collection has been initialized, and returns the result
         * as a Boolean.
         */
        private fun isCollectionInitialized(collectionDir: String): Boolean {
            return File(collectionDir, COLLECTION_BASE_FILE_NAME).exists()
                    && File(collectionDir, COLLECTION_SPAN_FILE_NAME).exists()
                    && File(collectionDir, COLLECTION_META_FILE_NAME).exists()
        }

        fun registerWordGroupInDatabase(context: Context, data: CollectionData) {
            CollectionDatabase.getDatabase(context).wordGroupDao().insert(data)
        }

        @JvmStatic
        fun openCreateNewWordGroupUi(context: Context): Boolean {
            /**
             * Opens the "Create Word Group" UI. When called, this method will broadcast the action
             * com.ryanhtech.vocabulario.action.collection.CRATE_WORD_GROUP to prompt the user to
             * create a new word group.
             *
             * @param context The context to use while processing Activity launch.
             * @return If the action has been sent successfully.
             * @throws IllegalAccessException If the caller is unexpected
             *
             * @since Initial release
             * @author Ryanhtech Labs
             */

            // First, check if it is us that are requesting to start the activity.
            val callerPackageName = context.applicationContext.packageName

            if (callerPackageName != BuildConfig.APPLICATION_ID) {
                // There is a serious problem, because the caller is not part of our app.
                throw IllegalAccessException("The package $callerPackageName is not authorized to call" +
                " some APIs, including this one, that are part of Vocabulario.")
            }

            try {
                // Create the new Intent with the action
                val broadcastIntent = Intent(
                    context.getString(R.string.action_collection_add_word_group))

                // We have our Intent. Now try to send the broadcast using it. The receiver will
                // require the RECEIVE_COLLECTION_EVENTS permission to be able to receive it.
                context.sendBroadcast(
                    broadcastIntent/*, com.ryanhtech.vocabulario.Manifest.permission.RECEIVE_COLLECTION_EVENTS*/)
            }
            catch (e: Exception) {
                // We have an exception here, so log it and return False.
                Log.e("Vb/CollectionManager", "An error occurred while attempting to show the " +
                     "UI to add a new word group to the user.")
                return false
            }

            return true
        }
    }
}