/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.tools.quiz

import com.ryanhtech.vocabulario.tools.collection.CollectionWord
import com.ryanhtech.vocabulario.tools.collection.wordpointers.WordPointer

object QuizContentSelector {
    val KNOWN_WORD_PICKING_LUCK = (1..2)

    /**
     * This tool picks adapted words from a list of word pointers. It is used
     * to pick words that will be used in the quiz.
     */
    fun pickAdaptedWords(wordPointerList: List<WordPointer>): List<WordPointer> {
        // Start the picking
        val finalPickedWordsList = mutableListOf<WordPointer>()
        val wordPointerListCopy = wordPointerList.shuffled()
        for (wordPointer in wordPointerListCopy) {
            // Be sure to use the copy, or the same words will be picked every time
            if (wordPointer.mWord.mMasteringLevel == CollectionWord.MASTERING_HIGH
                && KNOWN_WORD_PICKING_LUCK.random() == 1) {
                // Add the word to the list
                finalPickedWordsList.add(wordPointer)
            }
            else if (wordPointer.mWord.mMasteringLevel != CollectionWord.MASTERING_HIGH) {
                finalPickedWordsList.add(wordPointer)
            }
        }

        if (finalPickedWordsList.size < 10) {
            // Wow! Our list is too small!
            throw IllegalStateException("Word pointer list size too small")
        }

        return finalPickedWordsList
    }
}