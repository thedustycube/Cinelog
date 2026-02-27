package com.dustycube.cinelog.data.models

import java.time.LocalDateTime

data class TvShow(
    override val id: Int,
    val name: String,
    override val adult: Boolean, // new
    override val overview: String, // new
    override val popularity: Double, // new
    override val poster_path: String,
    override val vote_average: Double?,
    override val vote_count: Int, // new
    override val media_type: String, // reworked
    override val watchStatus: WatchStatus,
    override val lastUpdatedTimeStamp: LocalDateTime
) : UserWatchItem

// backdrop_path: String
// first_air_date: String - end date
// genre_ids: List<Int>
// origin_country: List<String>
// original_language: String
// original_name: String

data class TrendingTvResponse(
    val results: List<TvShow>
)