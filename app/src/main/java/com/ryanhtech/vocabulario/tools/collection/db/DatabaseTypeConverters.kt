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