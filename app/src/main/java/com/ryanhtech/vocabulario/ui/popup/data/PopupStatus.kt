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

package com.ryanhtech.vocabulario.ui.popup.data

/**
 * This class stores the Popup status (exit code, stack trace, and more).
 *
 * @see PopupDataContainer
 */
class PopupStatus {
    companion object {
        /**
         * When the Popup had no errors.
         */
        const val POPUP_STATUS_OK = 0

        /**
         * When the Popup occurred an error.
         */
        const val POPUP_STATUS_ERR = 1

        /**
         * When the user cancelled the Popup operation.
         */
        const val POPUP_STATUS_CANCEL = -1
    }

    // The status code. It can be anything, not only the constants defined in
    // the companion object.
    private var mPopupStatusCode = POPUP_STATUS_OK

    /**
     * Returns the status code of the Popup.
     *
     * @return The Popup status code.
     */
    fun getStatusCode(): Int = mPopupStatusCode

    /**
     * Sets the status code of the Popup.
     *
     * @param statusCode The status code to set.
     */
    fun setStatusCode(statusCode: Int) {
        mPopupStatusCode = statusCode
    }
}