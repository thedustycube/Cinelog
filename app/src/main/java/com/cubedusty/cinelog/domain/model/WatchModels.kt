package com.cubedusty.cinelog.domain.model

import com.cubedusty.cinelog.domain.model.MediaType
import com.cubedusty.cinelog.domain.model.WatchStatus
import java.time.LocalDate
import java.time.LocalDateTime

interface UserWatchItem {
    val id: Int
    val title: String
    val posterUrl: Int
    val mediaType: MediaType
    val watchStatus: WatchStatus
    val lastUpdatedTimeStamp: LocalDateTime
}

data class Movie(
    override val id: Int,
    override val title: String,
    override val posterUrl: Int,
    val director: String?,
    val releaseDate: LocalDate?,
    val overallRating: Double?,
    override val mediaType: MediaType = MediaType.MOVIE,
    override val watchStatus: WatchStatus = WatchStatus.NONE,
    override val lastUpdatedTimeStamp: LocalDateTime = LocalDateTime.now()
) : UserWatchItem

data class TvShow(
    override val id: Int,
    override val title: String,
    override val posterUrl: Int,
    val releaseDate: LocalDate?,
    val endDate: LocalDate?,
    val overallRating: Double?,
    override val mediaType: MediaType = MediaType.TV_SERIES,
    override val watchStatus: WatchStatus = WatchStatus.NONE,
    override val lastUpdatedTimeStamp: LocalDateTime = LocalDateTime.now()
) : UserWatchItem