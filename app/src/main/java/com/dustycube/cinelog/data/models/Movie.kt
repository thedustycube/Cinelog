package com.dustycube.cinelog.data.models

import java.time.LocalDateTime

data class Movie(
    override val id: Int,
    val title: String,
    override val adult: Boolean, // new
    override val overview: String, // new
    override val popularity: Double, // new
    override val poster_path: String,
    val director: String?,
    val release_date: String?, // localdate to string
    override val vote_average: Double?,
    override val vote_count: Int, // new
    override val media_type: String, // reworked
    override val watchStatus: WatchStatus,
    override val lastUpdatedTimeStamp: LocalDateTime
) : UserWatchItem

// backdrop_path: String
// genre_ids: List<Int>
// original_language: String
// original_title: String
// video: Boolean

data class TrendingMovieResponse(
    val results: List<Movie>
)

