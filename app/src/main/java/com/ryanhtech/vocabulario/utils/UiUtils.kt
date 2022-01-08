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

package com.ryanhtech.vocabulario.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.system.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.tools.collection.db.CollectionData
import java.util.*

class UiUtils {
    fun getPersonNameHello(applicationContext: Context): String {
        /**
         * Gets the person name with annotations. For example:
         *
         *             ¡Buenos días, Arturo!
         *
         * This function is used inside ActivityMain.
         */
        val name: String = DataManager.getName(applicationContext)

        /**
         * If the name is null, just say "Hello!".
         */

         ?: return applicationContext.getString(R.string.hi)

        /**
         * Determine wherever you should say "Good morning", "Good afternoon", or "Good night".
         *
         * Between 6AM to 12AM, say "Good morning".
         * Between 12AM to 8PM, say "Good afternoon".
         * Between 8PM to 6AM, say "Good night".
         *
         */

        val calendarData = Calendar.getInstance()
        val hour = calendarData.get(Calendar.HOUR_OF_DAY)
        var textToDisplay = ""

        if (hour >= 20 || hour < 6) {
            textToDisplay = applicationContext.getString(R.string.good_night)
        }
        if (hour in 6..11) {
            textToDisplay = applicationContext.getString(R.string.good_morning)
        }
        if (hour in 12..19) {
            textToDisplay = applicationContext.getString(R.string.good_afternoon)
        }
        if (textToDisplay == "") {
            throw NullPointerException("textToDisplay mustn't be \"\"!!!")
        }

        // Add the name to the textToDisplay
        textToDisplay += " $name!"

        return textToDisplay
    }

    companion object {
        fun animateEditTextIncorrect(editText: EditText) {
            animateViewIncorrectValue(editText)
        }

        fun animateViewIncorrectValue(view: View?, intensity: Int = 1) {
            if (view == null) return

            // Create a SpringAnimation
            val springAnimation = view.let { img ->
                SpringAnimation(img, DynamicAnimation.TRANSLATION_X, 0f).apply {
                    spring.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
                }
            }

            // Move the ImageView to the right to make the SpringAnimation work
            view.translationX = 100F * intensity

            springAnimation.start()
        }

        fun formatDateToCollectionDescriptor(collectionData: CollectionData): String {
            /**
             * Formats the text to the Collection WordGroup descriptor, like:
             * Today - 12 word(s)
             */

            val collectionDate = collectionData.date
            var dateText = "${collectionDate.get(Calendar.DAY_OF_MONTH)}/${collectionDate.get(Calendar.MONTH) + 1}/${collectionDate.get(Calendar.YEAR)}"

            if (DateUtils.compareCalendarDates(collectionDate, Calendar.getInstance())) {
                // The two calendars are the same
                dateText = Vocabulario.getContext().getString(R.string.today)
            }
            if (Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_YEAR, collectionDate.get(Calendar.DAY_OF_YEAR) - 1)
                    }.get(Calendar.DAY_OF_YEAR) == collectionDate.get(Calendar.DAY_OF_YEAR)
                && Calendar.getInstance().get(Calendar.YEAR) == collectionDate.get(Calendar.YEAR)) {
                // The Collection has been saved yesterday
                dateText = Vocabulario.getContext().getString(R.string.yesterday)
            }

            return "$dateText - ${collectionData.content.count()} ${Vocabulario.getContext().getString(R.string.words_element).lowercase()}"
        }

        fun getStatusBarHeight(context: Context): Int {
            /**
             * Gets the Android system status bar height.
             *
             * @return Status bar height
             */

            var result = 0
            val statusBarHeightResId = context.resources.getIdentifier(
                "status_bar_height", "dimen", "android"
            )

            if (statusBarHeightResId > 0) {
                result = context.resources.getDimensionPixelSize(statusBarHeightResId) - 8
            }

            return result
        }
    }
}