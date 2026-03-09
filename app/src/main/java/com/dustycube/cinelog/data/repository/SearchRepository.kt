package com.dustycube.cinelog.data.repository

import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.models.SearchItem
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class SearchRepository(
    private val api: TMDBApiService,
    private val commonRepository: CommonRepository
) {
    val accessToken = commonRepository.fetchAccessToken()

    fun getFullWatchlist(): Flow<List<WatchlistItemEntity>> = commonRepository.getFullWatchlist()

    fun getSearchResultsWithStatus(query: String): Flow<List<SearchItem>> = combine(
        flow { emit(fetchSearchResults(query)) },
        getFullWatchlist()
    ) { searchResult, watchlist ->
        searchResult.map { searchItem ->
            val savedItem = watchlist.find { it.id == searchItem.id }
            searchItem.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    suspend fun fetchSearchResults(query: String): List<SearchItem> {
        return try {
            val response = api.getSearchResults(query = query, token = accessToken)
            response.results.map { searchItem ->
                SearchItem(
                    id = searchItem.id,
                    title = if (searchItem.media_type == "movie") searchItem.title else null,
                    name = if (searchItem.media_type == "tv") searchItem.name else null,
                    adult = searchItem.adult ?: false,
                    overview = searchItem.overview ?: "",
                    popularity = searchItem.popularity ?: 0.0,
                    poster_path = searchItem.poster_path,
                    vote_average = searchItem.vote_average ?: 0.0,
                    vote_count = searchItem.vote_count ?: 0,
                    media_type = searchItem.media_type ?: "",
                    watchStatus = WatchStatus.NONE,
                    lastUpdatedTimeStamp = LocalDateTime.now()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateWatchStatus(
        item: UserWatchItem,
        newStatus: WatchStatus
    ) {
        commonRepository.updateWatchStatus(item, newStatus)
    }
}