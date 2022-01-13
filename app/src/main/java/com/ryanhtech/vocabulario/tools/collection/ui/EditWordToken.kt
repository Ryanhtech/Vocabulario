/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.tools.collection.ui

import com.ryanhtech.vocabulario.tools.collection.CollectionWord
import com.ryanhtech.vocabulario.tools.collection.WordGroup

class EditWordToken(wordToEdit: CollectionWord?, group: WordGroup) {
    private var mWordToEdit = wordToEdit
    private val mGroup = group

    fun getWord(): CollectionWord? = mWordToEdit

    fun getGroup(): WordGroup = mGroup

    fun setGroupContent(list: List<CollectionWord>) {
        mGroup.updateWordList(list)
    }

    fun setWord(word: CollectionWord?) {
        mWordToEdit = word
    }
}