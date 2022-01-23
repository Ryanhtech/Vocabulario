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

package com.ryanhtech.vocabulario.internal.settings

/**
 * This provides the results of the name check method in
 * `UserSettings.checkIsNameValid(username: String)`.
 */
object NameCheckStatus {
    /**
     * The name is valid, and matches with the rules.
     */
    const val NAME_VALID = "validName"

    /**
     * The name is too short or too long.
     */
    const val NAME_OUT_OF_INDEX = "outOfIndex"

    /**
     * The name starts or finish with a whitespace.
     */
    const val NAME_WHITESPACE = "nameWhitespace"

    /**
     * The name doesn't respect the uppercase/lowercase
     * rules.
     */
    const val NAME_CASE_ERROR = "nameCaseError"
}