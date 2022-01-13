/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
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