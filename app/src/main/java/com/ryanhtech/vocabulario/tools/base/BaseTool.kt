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