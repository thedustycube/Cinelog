package com.dustycube.cinelog.data.repository

import android.util.Log
import com.dustycube.cinelog.BuildConfig
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.local.EpisodeEntity
import com.dustycube.cinelog.data.local.SeasonProgressEntity
import com.dustycube.cinelog.data.local.WatchlistDao
import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.mapper.toEntity
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.SearchItem
import com.dustycube.cinelog.data.model.Season
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class CommonRepository(
    private val api: TMDBApiService,
    private val dao: WatchlistDao
) {
    val accessToken = BuildConfig.TMDB_ACCESS_TOKEN

    fun fetchAccessToken(): String = accessToken

    fun getFullWatchlist(): Flow<List<WatchlistItemEntity>> = dao.getFullWatchlist()

    suspend fun getItemById(itemId: Int, mediaType: String): WatchItem? {
        return try {
            when (mediaType) {
                "movie" -> {
                    val response = api.getMovieDetailsById(id = itemId)
                    response.copy(
                        watchStatus = WatchStatus.NONE,
                        lastUpdatedTimeStamp = LocalDateTime.now()
                    )
                }
                "tv" -> {
                    val response = api.getTvShowDetailsById(id = itemId)
                    response.copy(
                        watchStatus = WatchStatus.NONE,
                        lastUpdatedTimeStamp = LocalDateTime.now(),
                        seasons = response.seasons.map { season ->
                            season.copy(
                                watchStatus = WatchStatus.NONE,
                                episodes = emptyList()
                            )
                        }
                    )
                }
                else -> {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSeasonDetails(showId: Int, seasonNumber: Int): Season? {
        return try {
            val response = api.getTvSeasonDetails(seriesId = showId, seasonNumber = seasonNumber)

            response.let { season ->
                val episodeEntities = season.episodes.map { episode ->
                    EpisodeEntity(
                        id = episode.id,
                        showId = showId,
                        season_number = seasonNumber,
                        episode_number = episode.episode_number,
                        name = episode.name,
                        overview = episode.overview,
                        air_date = episode.air_date,
                        runtime = episode.runtime,
                        still_path = episode.still_path,
                        isWatched = false
                    )
                }
                dao.upsertEpisodes(episodeEntities)
            }
            response
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateWatchStatus(
        item: WatchItem, newStatus: WatchStatus, updatedValue: Int = 0
    ) {
        val entity = when(item) {
            is Movie -> item.toEntity(newStatus)
            is TvShow -> item.toEntity(newStatus).copy(episodesWatched = item.episodesWatched + updatedValue)
            is SearchItem -> item.toEntity(newStatus)
            is WatchlistItemEntity -> item.copy(
                watchStatus = newStatus,
                lastUpdatedTimeStamp = LocalDateTime.now(),
                episodesWatched = item.episodesWatched + updatedValue
            )
            else -> return
        }
        if (item is TvShow && newStatus == WatchStatus.COMPLETED) {
            val progressList = item.seasons
                .filter { it.season_number > 0 }
                .map { season ->
                    SeasonProgressEntity(
                        showId = item.id,
                        season_number = season.season_number,
                        episodeWatched = season.episode_count,
                        episodeCount = season.episode_count,
                        watchStatus = WatchStatus.COMPLETED
                    )
                }
            dao.upsertAllSeasonProgress(progressList)
            dao.upsertItem(entity)
        } else if (newStatus != WatchStatus.NONE) {
            dao.upsertItem(entity)
        } else dao.removeFromWatchlist(item.id)
    }
}