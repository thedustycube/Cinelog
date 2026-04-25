package com.dustycube.cinelog.data.model

data class Season(
    val id: Int,
    val showId: Int,
    val air_date: String?,
    val episode_count: Int,
    val name: String?,
    val overview: String?,
    val poster_path: String?,
    val season_number: Int,
    var episodeWatched: Int = 0,
    var watchStatus: WatchStatus = WatchStatus.NONE,
    val episodes: List<Episode>
)