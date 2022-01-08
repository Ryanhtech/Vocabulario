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