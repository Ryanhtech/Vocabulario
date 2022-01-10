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

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ryanhtech.vocabulario.BuildConfig
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.annotations.DangerousOperation
import com.ryanhtech.vocabulario.internal.annotations.RequiresVocabularioUserInteraction

/**
 * This class makes the reset operations. To run such operation, you
 * must instantiate this class with the operation you want (in
 * VocabularioResetType) and then call .authenticate() to prompt the
 * user, and finally call .runOperation(context).
 *
 * @since initial version
 * @author Ryanhtech Labs
 * @see VocabularioResetType
 */
class VocabularioResetOperation(resetType: String, context: Context) {
    companion object {
        const val AUTHENTICATION_TYPE_PENDING = 0
        const val AUTHENTICATION_TYPE_DENIED = -1
        const val AUTHENTICATION_TYPE_GRANTED = 1
    }

    init {
        // Initialize the class before proceeding
        processInternalClassInitialization(resetType, context)
    }

    /**
     * Determines if the current instance is locked (we can't use
     * it in this case).
     */
    private var isInstanceLocked = false
    set (setAttempt) {
        // Don't allow reverting to false once it's set to true
        checkInstanceLockStatusAndRaise()
        field = setAttempt
    }

    /**
     * Determines if the current instance is authenticated by the user.
     */
    var authenticationStatus = AUTHENTICATION_TYPE_PENDING
    set (setAttempt) {
        // Don't allow variable change if it's not allowed
        if (!authorizeIsAuthenticatedModification) throw IllegalAccessException(
            "You can't change this variable! (Access denied)")
        // Don't allow setting to another value than the permitted ones
        if (setAttempt != AUTHENTICATION_TYPE_DENIED
            && setAttempt != AUTHENTICATION_TYPE_GRANTED
            && setAttempt != AUTHENTICATION_TYPE_PENDING) {
            throw IllegalStateException("You can set authenticationStatus only to Vocabulario" +
                "ResetOperation.AUTHENTICATION_* attributes")
        }
        // Don't allow reverting to anything once it's set to anything else but PENDING
        if (field == AUTHENTICATION_TYPE_GRANTED && field == AUTHENTICATION_TYPE_DENIED) {
            field = setAttempt
        }
    }

    /**
     * If this variable is set to false, you can't modify
     * the authenticationStatus variable.
     */
    private var authorizeIsAuthenticatedModification = true

    //
    // For more info on these, read the class' JavaDoc.
    //
    private lateinit var mResetType: String
    private lateinit var mContext: Context

    /**
     * This method initializes the class by checking if everything is ready
     * to go.
     */
    private fun processInternalClassInitialization(resetType: String, context: Context) {
        checkInstanceLockStatusAndRaise()

        // Don't forget to delete the permission to change the authentication status!
        authorizeIsAuthenticatedModification = false

        // First, check if the context is part of our app.
        val thisAppPackageName = BuildConfig.APPLICATION_ID
        val otherAppPackageName = context.applicationContext.packageName

        if (thisAppPackageName != otherAppPackageName) {
            // The package names doesn't match, so lock the class and raise an exception.
            lockdownInstanceAndThrowException(
                IllegalStateException("You can't instantiate this class if you are an external " +
                        "app! (Access denied)"))
        }

        // Now check if the reset type actually exists (the caller might want to confuse
        // us ;))
        if (resetType != VocabularioResetType.TYPE_RESET_COLLECTION
            && resetType != VocabularioResetType.TYPE_RESET_FULL
            && resetType != VocabularioResetType.TYPE_RESET_LOCAL
            && resetType != VocabularioResetType.TYPE_RESET_UNINSTALL) {
            // Raise the exception
            lockdownInstanceAndThrowException(
                IllegalStateException("The provided reset type doesn't exists. Type: \"$resetType\""))
        }

        // Then we have our reset type. Copy it into mResetType, and the context as
        // well.
        mContext = context
        mResetType = resetType
    }

    /**
     * Authenticates the user. The user must give a clear consent to the operation.
     * When given, the variable authenticationStatus is set to true so you can perform
     * operations. If the user already saw this dialog, you can't show it again, unless
     * you create a new instance.
     */
    fun authenticate(posClickCallback: Thread,
                     negClickCallback: Thread) {
        if (authenticationStatus != AUTHENTICATION_TYPE_PENDING) return

        AlertDialog.Builder(mContext).apply {
            setCancelable(false)
            setTitle(R.string.reset_operation_consent_title)
            setMessage(R.string.reset_operation_consent_description)
            setNegativeButton(R.string.cancel) { _, _ ->
                // The user doesn't consent to any operation, so cancel everything.
                authorizeIsAuthenticatedModification = true
                authenticationStatus = AUTHENTICATION_TYPE_DENIED
                negClickCallback.start()
            }
            setPositiveButton(R.string.yes) { _, _ ->
                // The user gave the permission.
                authorizeIsAuthenticatedModification = true
                authenticationStatus = AUTHENTICATION_TYPE_GRANTED
                posClickCallback.start()
            }
            show()
        }
    }

    /**
     * This method runs the requested operation. You must pass the same context
     * argument that you passed to instantiate the class.
     * There is no coming back, please know what you are doing when you run this.
     *
     * @throws IllegalAccessException
     * @author Ryanhtech Labs
     * @since initial version
     */
    @RequiresVocabularioUserInteraction
    @DangerousOperation
    fun runOperation(context: Context) {
        // Pass the required arguments to startRequestedOperationInternal to
        // prepare and run the operation
        startRequestedOperationInternal(context, mResetType)
    }

    /**
     * This prepares the requested operation and runs it.
     */
    private fun startRequestedOperationInternal(context: Context, requestedOperation: String) {
        // Check everything before doing anything
        checkInstanceLockStatusAndRaise()

        if (context != mContext) {
            // If the given context doesn't match with the required context,
            // someone is maybe trying to steal our permissions.
            throw IllegalAccessException("The caller doesn't have permission to run this")
        }

        if (authenticationStatus != AUTHENTICATION_TYPE_GRANTED
            && requestedOperation != VocabularioResetType.TYPE_RESET_UNINSTALL) {
            // Wait -- is the caller taking us for noobs?
            throw IllegalAccessException("This instance doesn't have a consent by the user")
        }

        // Now everything is OK.

        // Start the requested operation
        if (requestedOperation == VocabularioResetType.TYPE_RESET_UNINSTALL) {
            runUninstallOperation()
        }
    }

    @DangerousOperation
    @RequiresVocabularioUserInteraction
    private fun runUninstallOperation() {
        // Create the Intent to uninstall the app
        val deleteIntent = Intent(Intent.ACTION_DELETE)
        deleteIntent.data = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        deleteIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mContext.startActivity(deleteIntent)

        // Now that we have launched our prompt, just return! The Activity that called
        // this must implement onResume() to detect cancellation.
        return
    }

    /**
     * This method must be called when other methods are called. It verifies if the
     * code that instantiates the class is authorized to instantiate it.
     *
     * @throws IllegalAccessException
     */
    private fun checkInstanceLockStatusAndRaise() {
        if (isInstanceLocked) throw IllegalAccessException("The instance is locked. Please " +
            "create a new one properly with the appropriate permissions. Don't use hacky ways.")
    }

    /**
     * This method locks the instance and raises the provided Exception. It is useful if
     * suspicious activity is detected in the class to avoid security issues. We are
     * talking data loss, after all.
     */
    private fun lockdownInstanceAndThrowException(exception: Exception) {
        isInstanceLocked = true
        throw exception
    }
}