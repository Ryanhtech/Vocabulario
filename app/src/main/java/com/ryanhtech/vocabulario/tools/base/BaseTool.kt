/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.tools.base

open class BaseTool {
    /**
     * Base tool class for ¡Vocabulario!.
     */

    companion object {
        /**
         * The tool name string resource ID. It might be changed
         * over time, so we will put this in a const val.
         */
        open val TOOL_NAME_RES: Int = 2

        /**
         * The tag used in Logcat.
         */
        open val TAG: String = "BaseTool"
    }
}