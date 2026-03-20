package com.dustycube.cinelog.data.model

import java.time.LocalDateTime

data class Movie(
    override val id: Int,
    val title: String?,
    override val adult: Boolean?,
    override val overview: String?,
    override val popularity: Double?,
    override val poster_path: String?,
    val director: String?,
    val release_date: String?,
    val original_language: String?,
    override val vote_average: Double?,
    override val vote_count: Int?,
    val credits: Credits?,
    override val media_type: String?,
    override val watchStatus: WatchStatus,
    override val lastUpdatedTimeStamp: LocalDateTime
) : WatchItem

// backdrop_path: String
// genre_ids: List<Int>
// original_title: String
// video: Boolean

data class TrendingMovieResponse(
    val results: List<Movie>
)

