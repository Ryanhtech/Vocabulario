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

package com.ryanhtech.vocabulario.internal.reset

/**
 * This object provides different types of reset operations. You can
 * use it to pass a value into VocabularioResetOperation to instantiate
 * a new VocabularioResetOperation to perform reset operations.
 *
 * @see VocabularioResetOperation
 * @author Ryanhtech Labs
 * @since initial version
 */
object VocabularioResetType {
    /**
     * Resets the Collection data only. It's useful if you need to change
     * your school or teacher, or when you start a new year.
     *
     * @since initial version
     */
    const val TYPE_RESET_COLLECTION = "resetCollection"

    /**
     * Resets ONLY the data of Vocabulario. It won't ask the system to delete
     * the data, Vocabulario will do it itself. It's useful if you want to
     * control the app for your organization. Permissions and system settings
     * related to this app will not change.
     *
     * @since initial version
     */
    const val TYPE_RESET_LOCAL = "resetLocal"

    /**
     * This is like the TYPE_RESET_LOCAL attribute, but it asks the system to
     * clear the data instead. It will reset the permissions, and all system
     * settings related to the app. It's useful for troubleshooting or to
     * start over.
     *
     * @since initial version
     */
    const val TYPE_RESET_SYSTEM = "resetSystem"

    /**
     * This will prompt the user to uninstall the app. No authentication is
     * required to run this action.
     *
     * @since initial version
     */
    const val TYPE_RESET_UNINSTALL = "resetUninstall"
}