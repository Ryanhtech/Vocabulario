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

import android.content.Context
import com.ryanhtech.vocabulario.tools.collection.CollectionWord
import com.ryanhtech.vocabulario.tools.collection.wordpointers.CollectionWordContentManager

class QuizPlay(words: List<CollectionWord>) {
    private val mWordsToPass: MutableList<CollectionWord> = words.toMutableList()

    fun getRandomWord(): CollectionWord {
        val randomIndex = (0..mWordsToPass.size).random()

        val chosenWord = mWordsToPass[randomIndex]
        mWordsToPass.removeAt(randomIndex)  // Remove the word because we played it

        return chosenWord
    }

    companion object {
        fun cookInstance(context: Context): QuizPlay {
            /**
             * Cook an instance of QuizPlay. We cook it because
             * we proceed to the instantiation by ourselves, like
             * a chef cooking his cake. He doesn't get it from nowhere,
             * instead, he makes it himself (good chefs do).
             */

            // Ingredient #1: word pointers
            // We get the word pointers from the CollectionWordContentManager.
            val pointers = CollectionWordContentManager.listPointersAndContent(context)

            // Then, we select the words to put in the quiz. For that, call our friend
            // QuizContentSelector.
            var pickedWords: List<CollectionWord>? = null
            try {
                pickedWords = CollectionWordContentManager.convertWordPointerListToWordList(
                    QuizContentSelector.pickAdaptedWords(pointers))
            } catch (e: IllegalStateException) {
                // Pass for now...
            }

            if (pickedWords == null) throw NullPointerException(
                "pickedWords is null; cannot give burned cake to waiter (an IllegalStateException " +
                "has been thrown from QuizContentSelector.pickAdaptedWords(pointers))\n" +
                "Technical info: context = $context\npointers = $pointers")

            return QuizPlay(pickedWords)
        }
    }
}