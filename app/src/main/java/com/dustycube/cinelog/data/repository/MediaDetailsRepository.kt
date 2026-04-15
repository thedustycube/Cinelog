package com.dustycube.cinelog.data.repository

import com.dustycube.cinelog.data.local.SeasonProgressEntity
import com.dustycube.cinelog.data.local.WatchlistDao
import com.dustycube.cinelog.data.model.Season
import com.dustycube.cinelog.data.model.WatchStatus

class MediaDetailsRepository(
    private val dao: WatchlistDao
) {
    suspend fun updateSeasonProgress(season: Season, newStatus: WatchStatus, progress: Int) {
        if (progress == 0 && newStatus == WatchStatus.NONE) return
        val seasonItem = SeasonProgressEntity(
            showId = season.showId,
            seasonNumber = season.season_number,
            episodeWatched = progress,
            watchStatus = newStatus
        )
        if (progress == 0 || progress < 0 || progress > season.episode_count) {
            when (newStatus) {
                WatchStatus.WATCHING -> seasonItem.watchStatus = newStatus
                WatchStatus.COMPLETED -> {
                    seasonItem.watchStatus = newStatus
                    seasonItem.episodeWatched = season.episode_count
                }
                else -> {  }
            }
        } else if (progress == season.episode_count){
            seasonItem.watchStatus = WatchStatus.COMPLETED
        } else {
            seasonItem.watchStatus = WatchStatus.WATCHING
            seasonItem.episodeWatched = progress
        }
        dao.upsertSeasonProgress(seasonItem)
    }
}