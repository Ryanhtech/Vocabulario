/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
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