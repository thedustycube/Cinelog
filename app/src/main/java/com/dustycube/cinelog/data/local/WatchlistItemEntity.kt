package com.dustycube.cinelog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import java.time.LocalDateTime

@Entity(tableName = "watchlist")
data class WatchlistItemEntity(
    @PrimaryKey override val id: Int,
    override val adult: Boolean,
    override val overview: String,
    override val popularity: Double,
    override val poster_path: String?,
    override val vote_average: Double?,
    override val vote_count: Int,
    override val media_type: String,
    override val watchStatus: WatchStatus,
    override val lastUpdatedTimeStamp: LocalDateTime,
    val title: String?,
    val name: String?,
    val director: String?,
    val release_date: String?,
    val watchStatusOrder: Int
) : UserWatchItem