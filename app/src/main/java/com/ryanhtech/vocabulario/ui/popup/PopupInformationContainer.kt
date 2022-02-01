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

package com.ryanhtech.vocabulario.ui.popup

import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity

/**
 * This class manages and stores the PopupInformation instances. You can use
 * it to get the PopupDataContainer out of it, or to get the PopupStatus info
 * from a PopupFragment instance. This is an **internal** Vocabulario system
 * class, so you shouldn't implement it directly. Use
 * `VocabularioActivity.displayPopupFragment()` to do this instead, if your
 * Activity extends `VocabularioActivity`.
 *
 * @see PopupFragment
 * @see PopupInformation
 * @see VocabularioActivity.displayPopupFragment
 *
 * @author Ryanhtech Labs
 * @since Initial version
 */
object PopupInformationContainer {
    /**
     * This map contains the PopupInformation IDs that matches with the corresponding
     * fragment.
     */
    val mPopupInfoList: MutableList<PopupInformation> = mutableListOf()

    /**
     * This adds a new PopupFragment instance to the fragment list. An ID will be
     * created and will be associated to it. The created `PopupInformation` instance
     * will be returned.
     */
    fun registerPopupFragment(popupFragment: PopupFragment): PopupInformation {
        // First, create a new PopupInformation with the fragment instance
        val lCurrentPopupInformation = PopupInformation(popupFragment)

        // Add the popup info to the list
        mPopupInfoList.add(lCurrentPopupInformation)

        // Return the popup information
        return lCurrentPopupInformation
    }
}