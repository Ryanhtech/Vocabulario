/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.tools.collection.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ryanhtech.vocabulario.tools.collection.wordpointers.WordPointer
import java.util.*

@Entity(tableName="word_group")
data class CollectionData (
    @PrimaryKey(autoGenerate = true) val wid: Int,
    @ColumnInfo(name="date") val date: Calendar,
    @ColumnInfo(name="title") val title: String,
    @ColumnInfo(name="content") val content: List<WordPointer>,
)