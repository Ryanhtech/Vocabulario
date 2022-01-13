/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.tools.collection.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ryanhtech.vocabulario.tools.collection.CollectionProperties

@Database(entities = [CollectionData::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverters::class)
abstract class CollectionDatabase : RoomDatabase() {

    abstract fun wordGroupDao(): CollectionDao

    companion object {
        @Volatile
        private var INSTANCE: CollectionDatabase? = null

        fun getDatabase(context: Context): CollectionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CollectionDatabase::class.java,
                    CollectionProperties.COLLECTION_DB_FILE_NAME
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}