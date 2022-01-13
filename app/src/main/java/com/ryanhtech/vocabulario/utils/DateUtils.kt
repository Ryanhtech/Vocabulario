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

package com.ryanhtech.vocabulario.utils

import java.util.*

object DateUtils {
    fun compareCalendarDates(calendar1: Calendar, calendar2: Calendar): Boolean {
        // Returns true if the two calendars are set to the same date.

        val date1: Array<Int> = arrayOf(
            calendar1.get(Calendar.DAY_OF_YEAR),
            calendar1.get(Calendar.YEAR)
        )

        val date2: Array<Int> = arrayOf(
            calendar2.get(Calendar.DAY_OF_YEAR),
            calendar2.get(Calendar.YEAR)
        )

        return date1.contentEquals(date2)
    }
}