/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
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