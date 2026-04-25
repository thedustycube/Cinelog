package com.dustycube.cinelog.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WatchlistItemEntity::class, SeasonProgressEntity::class, EpisodeEntity::class], version = 8)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}