/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

/*
 * This content comes from StackOverflow: https://stackoverflow.com
 */
package com.ryanhtech.vocabulario.security

import java.security.MessageDigest
import kotlin.experimental.and

object Hash {
    fun getHash(input: String, algorithm: String): String {
        /*
         * Returns the SHA-512 of the provided String.
         */
        val msgDgst = MessageDigest.getInstance(algorithm)

        val digest = msgDgst.digest(input.toByteArray())

        val strBuilder = StringBuilder()

        for (b in digest) {
            strBuilder.append(((b and 0xff.toByte()) + 0x100).toString(16).substring(1))
        }

        return strBuilder.toString()
    }

    fun getHash(input: String): String {
        return getHash(input, "SHA-512")
    }
}