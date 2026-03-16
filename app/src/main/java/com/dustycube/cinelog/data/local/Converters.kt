package com.dustycube.cinelog.data.local

import androidx.room.TypeConverter
import com.dustycube.cinelog.data.model.WatchStatus
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromWatchStatus(status: WatchStatus): String = status.name

    @TypeConverter
    fun toWatchStatus(value: String): WatchStatus = WatchStatus.valueOf(value)

    @TypeConverter
    fun fromTimestamp(date: LocalDateTime): String = date.toString()

    @TypeConverter
    fun toTimestamp(value: String): LocalDateTime = LocalDateTime.parse(value)
}