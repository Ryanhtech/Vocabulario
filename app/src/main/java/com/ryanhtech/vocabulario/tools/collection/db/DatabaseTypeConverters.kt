/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.tools.collection.db

import androidx.room.TypeConverter
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.tools.collection.wordpointers.WordPointer
import java.util.*

class DatabaseTypeConverters {
    @TypeConverter
    fun collectionDataToJson(value: List<WordPointer>?): String = Vocabulario.getGson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Vocabulario.getGson().fromJson(value, CollectionDataListToken().rawType)
                                                                as List<WordPointer>

    @TypeConverter
    fun calendarToJson(value: Calendar): String = Vocabulario.getGson().toJson(value)

    @TypeConverter
    fun jsonToCalendar(value: String): Calendar = Vocabulario.getGson().fromJson(value, Calendar::class.java)
}