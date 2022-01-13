/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.tools.collection

import com.ryanhtech.vocabulario.tools.collection.ui.exceptions.InvalidTitleException
import java.util.*

class WordGroup {
    private var groupList: MutableSet<CollectionWord> = mutableSetOf()
    var title: String = ""
    var date: Calendar = Calendar.getInstance()

    /**
     * Adds a word to the WordGroup.
     *
     * @param word  CollectionWord object to add.
     */
    fun append(word: CollectionWord) {
        if (groupList.contains(word)) return
        groupList.add(word)
    }

    /**
     * Deletes a word of the WordGroup.
     *
     * @param word  CollectionWord instance to delete.
     */
    fun delete(word: CollectionWord) {
        try {
            groupList.remove(word)
        } catch (_: Exception) {}
    }

    /**
     * Updates the title of the WordGroup.
     *
     * @param userTitle  Title to set.
     */
    fun updateTitle(userTitle: String) {
        if (userTitle.length < CollectionProperties.WGROUP_TITLE_MAX_LENGTH) {
            title = userTitle
        }
    }

    /**
     * Updates the date of the WordGroup.
     *
     * @param userDate  The date to set.
     */
    fun setDateWg(userDate: Calendar) {
        date = userDate
    }

    /**
     * Extracts the WordGroup.
     *
     * @param willCheckIntegrity If the integrity of the WordGroup must
     * be verified before extraction.
     *
     * @see checkIntegrity
     */
    fun ext(willCheckIntegrity: Boolean = true): List<CollectionWord> {
        if (willCheckIntegrity) checkIntegrity()

        return groupList.toList()
    }

    /**
     * Verifies the integrity of the WordGroup.
     *
     * @throws InvalidTitleException when the title is invalid.
     */
    private fun checkIntegrity() {
        if (title.length > CollectionProperties.WGROUP_TITLE_MAX_LENGTH) throw InvalidTitleException(
                "Invalid title: ${title.length} long, expected ${CollectionProperties.WGROUP_TITLE_MAX_LENGTH} or less"
            )

        if (title.isEmpty()) throw InvalidTitleException("Title is blank")
    }

    /**
     * Extracts the words of the WordGroup.
     *
     * @throws MutableSet The words of the WordGroup (as CollectionWord in the MutableSet)
     */
    fun extWords(): MutableSet<CollectionWord> = groupList

    /**
     * Updates the word list.
     *
     * @param list The list to set
     */
    fun updateWordList(list: List<CollectionWord>) {
        val mutableSet = list.toMutableSet()
        groupList = mutableSet

        return
    }
}