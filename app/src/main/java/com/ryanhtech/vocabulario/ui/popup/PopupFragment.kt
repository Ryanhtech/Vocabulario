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

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.fragment.VocabularioFragment
import java.io.Serializable

/**
 * This class represents a Popup fragment.
 */
open class PopupFragment : VocabularioFragment(), Serializable {
    /**
     * The layout to inflate to the PopupFragment to display. You can use it like this in your
     * custom PopupFragment:
     *
     * `override val popupLayoutRes = R.layout.some_layout`
     *
     * The layout will then automatically be inflated when the fragment is initialized in the
     * `PopupContainerActivity`. It is as simple as that.
     */
    @LayoutRes
    open val popupLayoutRes: Int? = null

    /**
     * This is the default resource pointing to the text that should be displayed in the positive
     * button of the Popup. If the call to `getPosButtonText()` returned null or failed, then this
     * text will be displayed. You can use the `getPosButtonText` method to set a custom message
     * with formatted text for example. The default is `android.R.string.ok`.
     */
    @StringRes
    open val posButtonDefaultText: Int = android.R.string.ok

    /**
     * This is the default resource pointing to the text that will be displayed in the negative
     * button of the Popup. If the call to `getNegButtonText()` returned null or failed, then
     * this text will be displayed instead. You can use the `getNegButtonText` method to set a
     * custom message with formatted text for example. The default is `R.string.cancel`.
     */
    @StringRes
    open val negButtonDefaultText: Int = R.string.cancel

    /**
     * This is the default Popup title that should be displayed on the screen, at the top of the
     * Popup. This will be used if the call to `getTitleText()` returned null or failed. The
     * default is `R.string.app_name`.
     */
    @StringRes
    open val titleDefaultText: Int? = R.string.app_name

    /**
     * This is the default tint resource to apply to the positive button. It helps keeping the
     * context of the previous Activity in the Popup. The default is `R.color.primary_colour`,
     * but you can override it in your own Fragment.
     */
    @ColorRes
    open val posButtonTintRes: Int = R.color.primary_colour

    /**
     * Set this to `false` if you don't want any negative button on your Popup. It is useful in
     * some situations in which the negative button is useless.
     */
    open val hasNegativeButton: Boolean = true

    /**
     * This contains the PopupFragment's root View.
     */
    private lateinit var mPopupRootView: View

    /**
     * This contains the application's Context.
     */
    lateinit var applicationContext: Context

    /**
     * This contains the root view to use when finding views by ID.
     */
    lateinit var popupRootView: View

    // The layout inflater is here
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // If you don't have any layout return the super value
        val lFragmentLayout = popupLayoutRes
            ?: return super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the layout here
        mPopupRootView = inflater.inflate(lFragmentLayout, container, false)
        popupRootView = mPopupRootView

        // Initialize the application context
        applicationContext = requireActivity().applicationContext

        // Start the job to process the Fragment's job and return the root view to the system
        this.popupStartJob()
        return mPopupRootView
    }

    /**
     * This method is called when the fragment is requested to initialize. DO NOT perform long
     * tasks on this thread since it is the UI thread.
     *
     * @throws IllegalStateException when this method is called from the wrong thread
     */
    @UiThread
    open fun popupStartJob() {
        // Your job goes in your overridden method after the super call
        Log.i("PopupFragment", "Starting PopupFragment initialization job")

        // Check if we are running on the UI thread
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw IllegalStateException("This method (popupStartJob) must only be called on the " +
                "UI thread")
        }
    }

    /**
     * This method is called when the positive button is pressed.
     *
     * @return if the Popup must be dismissed or not.
     */
    open fun onPositiveButtonClick(): Boolean {
        // Return true by default.
        return true
    }

    /**
     * This method is called when the negative button is pressed.
     *
     * @return if the Popup must be cancelled.
     */
    open fun onNegativeButtonClick(): Boolean {
        // Return true by default here too.
        return true
    }

    /**
     * This method is called when you should return the Popup title. It will return the
     * `titleDefaultText` value by default, but you can provide a custom title by returning a
     * formatted text instead and by deleting the super call. For example:
     *
     * ```
     * override fun getTitleText(context: Context): String? {
     *     val startString = context.getString(R.string.some_string)
     *     val endString = Stuff.getStuffString()
     *     return startString + " " + endString
     * }
     * ```
     *
     * **Warning**: *Do not* return `""`. If you don't want any title, return `null` instead.
     *
     * @return the text to display as a [String]. If null, no title will be displayed.
     */
    open fun getTitleText(context: Context): String? {
        // [Note: this is the default super call]
        // If we don't have any title defined for this Popup return null so we do not see any title
        val defText = titleDefaultText
            ?: return null

        return context.getString(defText)
    }

    /**
     * This method is called when you should return the positive button text. This works like the
     * `getTitleText` method, but you can't return `null` for this one.
     *
     * @see getTitleText
     * @return the text to display in the positive button.
     */
    open fun getPositiveButtonText(context: Context): String {
        // Just return the string
        return context.getString(posButtonDefaultText)
    }

    /**
     * This method is called when you should return the negative button text. This works like the
     * `getTitleText` method, but you can't return `null` here. Use the `hasNegativeButton` variable
     * to determine if you should display the negative button, in which case this method has no
     * effect.
     *
     * @see getTitleText
     * @see hasNegativeButton
     * @return the text to display in the positive button.
     */
    open fun getNegativeButtonText(context: Context): String {
        // Just return the string here too
        return context.getString(negButtonDefaultText)
    }
}