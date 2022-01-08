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

package com.ryanhtech.vocabulario.tools.collection.wordpointers

import android.content.Context
import com.ryanhtech.vocabulario.system.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.tools.collection.CollectionWord
import java.io.File

object CollectionWordContentManager {
    // IMPORTANT: NEVER CHANGE THIS!!! If you do, the app will be unable
    // to find the words from the pointers in the Collection database!
    private const val COLLECTION_WORD_CONTENT_FILE_NAME = "collectionWordContent.vocabulario"
    private const val COLLECTION_CURRENT_WORD_POINTER_FILE_NAME = "collectionWordPointer.vocabulario"
    private const val COLLECTION_WORD_CONTENT_SEPARATOR = "\n"
    private const val COLLECTION_WORD_MAGIC_POINTER_CONTENT_SEPARATOR = "\u0541"

    private var cachedFileRawContents: String? = null

    /**
     * Initializes the word content file.
     */
    fun initFile(context: Context) {
        // Create a new file
        try {
            getFile(context).createNewFile()
        } catch (e: Exception) { }
    }

    private fun getFile(context: Context): File {
        return File(context.applicationContext.filesDir, COLLECTION_WORD_CONTENT_FILE_NAME)
    }

    fun generateWordPointerId(context: Context): Int {
        val baseFile = File(context.applicationContext.filesDir,
            COLLECTION_CURRENT_WORD_POINTER_FILE_NAME)

        if (!baseFile.exists()) baseFile.writeText("0")

        // Increment current pointer
        val id = baseFile.readText().toInt() + 1
        baseFile.writeText(id.toString())

        // Return pointer
        return id
    }

    fun registerWord(word: CollectionWord, context: Context): WordPointer {
        // Create the word pointer
        val pointer = WordPointer(generateWordPointerId(context).toString(), word)

        // Convert the word pointer to JSON
        val jsonPointer = convertPointerToJson(pointer)

        // Save the entry
        saveEntry(jsonPointer, pointer, context)

        return pointer
    }

    private fun convertPointerToJson(pointer: WordPointer): String {
        // Get the GSON
        val gsonInstance = Vocabulario.getGson()

        // Start the conversion and return it
        return gsonInstance.toJson(pointer)
    }

    private fun saveEntry(pointerJson: String, pointer: WordPointer, context: Context) {
        getFile(context).appendText("${pointer.mId}$COLLECTION_WORD_MAGIC_POINTER_CONTENT_SEPARATOR" +
            "$pointerJson$COLLECTION_WORD_CONTENT_SEPARATOR")
        invalidateCache()
    }

    fun getWordByPointer(pointer: WordPointer, context: Context): CollectionWord? {
        val list = listPointersAndContent(context)

        for (pointerElement in list) {
            if (pointerElement.mId == pointer.mId) {
                return pointerElement.mWord
            }
        }

        return null
    }

    fun listPointersAndContent(context: Context): List<WordPointer> {
        val fileContent = readFileContents(context)
        val rawPointerAndContentList: List<String> = fileContent.split(
            COLLECTION_WORD_CONTENT_SEPARATOR)

        // Convert all the pointers/content to WordPointer instances
        val pointerList = mutableListOf<WordPointer>()
        for (line in rawPointerAndContentList) {
            val separatedPointerAndContent = line.split(
                COLLECTION_WORD_MAGIC_POINTER_CONTENT_SEPARATOR)

            val pointer = WordPointer(separatedPointerAndContent[0],
                convertJsonToWord(separatedPointerAndContent[1]))

            pointerList.add(pointer)
        }

        return pointerList.toList()
    }

    private fun convertJsonToWord(json: String): CollectionWord {
        return Vocabulario.getGson().fromJson(json, CollectionWord::class.java) as CollectionWord
    }

    private fun readFileContents(context: Context): String {
        // Get the cache if it is available instead of reading the file directly
        val contents: String = if (cachedFileRawContents != null) cachedFileRawContents!!
            else getFile(context).readText()

        // Write the cache if it's unavailable
        if (cachedFileRawContents == null) cachedFileRawContents = contents

        return contents
    }

    private fun invalidateCache() {
        // To be used after a write file operation.
        cachedFileRawContents = null
    }

    fun convertWordPointerListToWordList(baseList: List<WordPointer>): List<CollectionWord> {
        val finalList = mutableListOf<CollectionWord>()

        for (pointer in baseList) {
            finalList.add(pointer.mWord)
        }

        return finalList.toList()
    }
}