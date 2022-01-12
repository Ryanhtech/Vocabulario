/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ryanhtech.vocabulario.internal.reset

import androidx.annotation.StringRes
import com.ryanhtech.vocabulario.R
import java.io.Serializable

/**
 * This class contains the information about a reset operation
 * that the user will see. It is made to pass it to
 * `ResetConfirmationActivity` so it displays useful info
 * about the operation that the user is about to do so it
 * is sure it will execute the correct action.
 *
 * @see VocabularioResetOperation
 * @author Ryanhtech Labs
 * @since initial version
 */
class VocabularioUserResetOperationInformation(operationType: String,
            @StringRes operationNameUi: Int, @StringRes operationDescriptionUi: Int): Serializable {
    var operationTypeLocal: String = operationType
    var operationNameUiLocal: Int = operationNameUi
    var operationDescriptionUiLocal: Int = operationDescriptionUi

    companion object {
        /**
         * This method returns the correct instance of this class based on
         * the requested operation type.
         *
         * @param resetOperationType The reset operation type to set.
         * @since initial version
         * @author Ryanhtech Labs
         * @throws IllegalStateException When the reset operation type doesn't exists
         */
        @JvmStatic
        fun getInstanceFromResetOperationType(resetOperationType: String)
            : VocabularioUserResetOperationInformation {
            @StringRes var lUiOperationTitle: Int = -1
            @StringRes var lUiOperationDescription: Int = -1

            // Define the properties that match with the corresponding reset
            // operation type
            if (resetOperationType == VocabularioResetType.TYPE_RESET_COLLECTION) {
                lUiOperationTitle = R.string.reset_collection
                lUiOperationDescription = R.string.reset_collection_descr
            }
            if (resetOperationType == VocabularioResetType.TYPE_RESET_LOCAL) {
                lUiOperationTitle = R.string.local_reset_title
                lUiOperationDescription = R.string.local_reset_descr
            }
            if (resetOperationType == VocabularioResetType.TYPE_RESET_SYSTEM) {
                lUiOperationTitle = R.string.system_reset_title
                lUiOperationDescription = R.string.system_reset_descr
            }
            if (resetOperationType == VocabularioResetType.TYPE_RESET_UNINSTALL) {
                lUiOperationTitle = R.string.uninstall_app_reset_ope_title
                lUiOperationDescription = R.string.uninstall_app_reset_ope_descr
            }

            if (lUiOperationTitle == -1 || lUiOperationDescription == -1) {
                // One of the variables hasn't been initialized, which means that
                // the passed argument was incorrect.
                throw IllegalStateException("The provided reset type (\"$resetOperationType\") " +
                    "doesn't exists")
            }

            // Create a new class instance and return it
            return VocabularioUserResetOperationInformation(resetOperationType, lUiOperationTitle,
                lUiOperationDescription)
        }
    }
}