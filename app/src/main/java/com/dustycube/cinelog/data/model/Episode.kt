package com.dustycube.cinelog.data.model

data class Episode(
    val id: Int,
    val air_date: String?,
    val episode_number: Int,
    val name: String?,
    val overview: String?,
    val runtime: Int,
    val season_number: Int,
    val show_id: Int,
    val still_path: String?
)