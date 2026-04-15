package com.dustycube.cinelog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dustycube.cinelog.data.model.WatchStatus
import java.time.LocalDateTime

@Entity(tableName = "season_progress")
data class SeasonProgressEntity(
    @PrimaryKey val seasonId: Int,
    val showId: Int,
    val watchStatus: WatchStatus,
    val episodeWatched: Int,
    val lastUpdatedTimestamp: LocalDateTime
)