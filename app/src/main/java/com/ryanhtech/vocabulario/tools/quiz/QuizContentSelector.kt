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