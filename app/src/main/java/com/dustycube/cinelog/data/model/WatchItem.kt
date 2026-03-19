package com.dustycube.cinelog.data.model

import java.time.LocalDateTime

interface WatchItem {
    val id: Int
    val adult: Boolean?
    val overview: String?
    val popularity: Double?
    val poster_path: String?
    val vote_average: Double?
    val vote_count: Int?
    val media_type: String?
    val watchStatus: WatchStatus
    val lastUpdatedTimeStamp: LocalDateTime
}