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