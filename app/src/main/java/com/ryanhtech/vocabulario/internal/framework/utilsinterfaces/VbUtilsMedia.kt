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

package com.ryanhtech.vocabulario.internal.framework.utilsinterfaces

import com.ryanhtech.vocabulario.internal.framework.VbUtils

/**
 * This class contains definitions for media utilities contained in [VbUtils].
 */
interface VbUtilsMedia {
    /**
     * This method will return `true` when music is being played on the phone, otherwise `false`.
     * This is used to determine if the user is listening to music while passing tests.
     *
     * @return `true` if music is being played, or `false` if not.
     */
    fun vbIsMusicPlaying(): Boolean {
        // Default value
        return false
    }
}