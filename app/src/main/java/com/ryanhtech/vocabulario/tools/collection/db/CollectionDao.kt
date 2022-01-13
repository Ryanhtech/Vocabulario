/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.tools.collection.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CollectionDao {
    @Query("SELECT * FROM word_group")
    fun getWords(): List<CollectionData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: CollectionData)

    @Query("DELETE FROM word_group")
    fun deleteWordGroup()
}