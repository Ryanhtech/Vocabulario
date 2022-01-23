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
 * This defines the type of supervision to apply to
 * the app. It only defines constants, like
 * `SUPERVISION_TYPE_SCHOOL`, and it helps defining
 * the settings regarding Vocabulario supervision.
 */
object VocabularioSupervisionType {
    /**
     * If the app is fully controlled by the user, use
     * this. It doesn't require any password to perform
     * dangerous operations, and leaves the app under
     * the user's full control.
     */
    const val SUPERVISION_TYPE_NONE = "noSupervision"

    /**
     * If the app must be supervised by the IT admin
     * owning the device. It helps preventing mischievous
     * behaviour from students using the device, and it
     * will prevent Vocabulario from being uninstalled
     * using a device admin. It will also prevent Collection
     * modification, and settings modification.
     */
    const val SUPERVISION_TYPE_SCHOOL = "schoolSupervision"
}