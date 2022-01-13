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

class CollectionWord(baseWord: String, targetWord: String) {

    companion object {
        const val MASTERING_NEW = 0x10000000
        const val MASTERING_LOW = 0x00000000
        const val MASTERING_MEDIUM = 0x00000001
        const val MASTERING_HIGH = 0x00000002
    }

    private val targetWordInstance = targetWord
    private val baseWordInstance = baseWord
    private var masteringLevel = MASTERING_NEW

    var mBaseWord = baseWord
    set(value) {
        if (value == targetWordInstance) field = value
    }
    var mTargetWord = targetWord
    set(value) {
        if (value == baseWordInstance) field = value
    }
    var mMasteringLevel = masteringLevel

    @Deprecated("Deprecated in Java", ReplaceWith("null"))
    fun extString(): String {
        // Format explanation:
        // Base word | Target Word | Word mastering level (-4 to 4)
        return "$mBaseWord|$mTargetWord|-4"
    }
}