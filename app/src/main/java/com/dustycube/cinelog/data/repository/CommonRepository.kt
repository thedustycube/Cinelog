package com.dustycube.cinelog.data.repository

import android.util.Log
import com.dustycube.cinelog.BuildConfig
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.local.WatchlistDao
import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.mapper.toEntity
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.SearchItem
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
                        lastUpdatedTimeStamp = LocalDateTime.now()
                    )

                }
                else -> {
                    Log.w("CommonRepository: ", "Unknown media type: $mediaType")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("CommonRepository: Failed to get item by id. Error: ", e.toString())
            null
        }
    }

    suspend fun updateWatchStatus(
        item: WatchItem, newStatus: WatchStatus
    ) {
        val entity = when(item) {
            is Movie -> item.toEntity(newStatus)
            is TvShow -> item.toEntity(newStatus)
            is SearchItem -> item.toEntity(newStatus)
            is WatchlistItemEntity -> item.copy(
                watchStatus = newStatus,
                lastUpdatedTimeStamp = LocalDateTime.now()
            )
            else -> return
        }
        if (newStatus != WatchStatus.NONE) dao.upsertItem(entity)
        else dao.removeFromWatchlist(item.id)
    }
}