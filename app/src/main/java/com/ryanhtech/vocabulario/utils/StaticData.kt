/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.utils

const val EASTEREGG_PUSH_TARGET:   Int = 8
const val EASTEREGG_STPRSD_TARGET: Int = 3

class StaticData {
    // This class contains data like random name lists, etc...

    companion object {
        // ----------------------------------------------------------------------
        // RANDOM NAMES
        // This part of the class provides random spanish names generation.
        // It has a set, and a function to get a random name as a String.

        private val spanishNames: Set<String> = setOf(

            "Arturo",
            "José",
            "Roberto",
            "María",
            "Blanca",
            "Iván",
            "Manuel",
            "Luis",
            "Micaela",
            "Jorge",
            "Yasmina",
            "Elián",
            "Juana",
            "Lorena",
            "Hugo",
            "Iñaki",
            "Babacar",
            "Javier",
            "Cheng",
            "Cristina",
            "Felipe",
            "Karima",
            "Ínigo",
            "Isabel",
            "Yuri",
            "Martínez",
            "Yamar",
            "Ana",
            "Antonio",
            "Eva",
            "Rosa",
            "Manuela",
            "Simón"
        )

        @JvmStatic
        fun getRandomSpanishName(isFromDifferentSource: Boolean = false,
                                 source: Set<String> = spanishNames): String {
            // Returns a random spanish name as a String.

            if (isFromDifferentSource && source === spanishNames) {
                throw IllegalAccessException("It was precised that the generation of names was " +
                                             "from another source, but it wasn't!")
            }

            // Return a random element from the set
            // (source.indices acts just like 0..source.count() - 1)
            return source.elementAt(
                    // We need to use source.indices and not source.count() because the app crashes
                    // if you are really unlucky because of a IndexOutOfBoundsException
                    (source.indices).random()
            )
        }

        /**
         * Setup static variables.
         */

        var setupStep: Int = 1

        var isSuggestionsPaused = false
    }
}