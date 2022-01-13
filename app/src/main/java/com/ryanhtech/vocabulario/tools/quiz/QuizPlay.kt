/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
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