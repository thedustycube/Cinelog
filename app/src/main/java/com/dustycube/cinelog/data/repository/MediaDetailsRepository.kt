package com.dustycube.cinelog.data.repository

import com.dustycube.cinelog.data.local.SeasonProgressEntity
import com.dustycube.cinelog.data.local.WatchlistDao
import com.dustycube.cinelog.data.model.Season
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class MediaDetailsRepository(
    private val dao: WatchlistDao,
    private val commonRepository: CommonRepository
) {
    suspend fun updateSeasonProgress(season: Season, newStatus: WatchStatus, progress: Int, show: TvShow) {
        val seasonItem = SeasonProgressEntity(
            showId = show.id,
            seasonNumber = season.season_number,
            episodeWatched = progress,
            episodeCount = season.episode_count,
            watchStatus = newStatus
        )
        if (newStatus == WatchStatus.NONE) {
            when (progress) {
                0 -> {
                    dao.deleteSeasonProgress(show.id, season.season_number)
                }
                in 1 until season.episode_count -> {
                    seasonItem.watchStatus = WatchStatus.WATCHING
                    dao.upsertSeasonProgress(seasonItem)
                    commonRepository.updateWatchStatus(show, WatchStatus.WATCHING)
                }
                season.episode_count -> {
                    seasonItem.watchStatus = WatchStatus.COMPLETED
                    dao.upsertSeasonProgress(seasonItem)
                    updateSeason(show)
                }
            }
        } else if (newStatus == WatchStatus.WATCHING) {
            if (progress == season.episode_count) {
                seasonItem.watchStatus = WatchStatus.COMPLETED
                dao.upsertSeasonProgress(seasonItem)
                updateSeason(show)
            }
        } else if (newStatus == WatchStatus.COMPLETED && progress < season.episode_count) {
            seasonItem.watchStatus = WatchStatus.WATCHING
            dao.upsertSeasonProgress(seasonItem)
            updateSeason(show)
        } else {
            dao.upsertSeasonProgress(seasonItem)
        }
    }

    fun getSeasonProgress(showId: Int): Flow<List<SeasonProgressEntity>> {
        return dao.getSeasonProgress(showId)
    }

    suspend fun updateSeason(show: TvShow) {
        val progressList = dao.getSeasonProgress(show.id).first().filter { it.seasonNumber != 0 }
        if(progressList.isEmpty()) {
            commonRepository.updateWatchStatus(show, WatchStatus.NONE)
            return
        }
        if (progressList.size == show.number_of_seasons) {
            val isWatching = progressList.any { it.watchStatus == WatchStatus.WATCHING }
            if (isWatching) {
                commonRepository.updateWatchStatus(show, WatchStatus.WATCHING)
            } else commonRepository.updateWatchStatus(show, WatchStatus.COMPLETED)
        } else {
            commonRepository.updateWatchStatus(show, WatchStatus.WATCHING)
        }
    }
}