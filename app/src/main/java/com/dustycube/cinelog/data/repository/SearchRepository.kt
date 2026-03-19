package com.dustycube.cinelog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.model.SearchItem
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import com.dustycube.cinelog.data.paging.UniversalPagingSource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class SearchRepository(
    private val api: TMDBApiService,
    private val commonRepository: CommonRepository
) {
    private val accessToken = commonRepository.fetchAccessToken()

    fun getSearchPagingFlow(query: String): Flow<PagingData<SearchItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                UniversalPagingSource { page ->
                    val response = api.getSearchResults(query = query, page = page, token = accessToken)
                    response.results.map { searchItem ->
                        searchItem.copy(
                            watchStatus = WatchStatus.NONE,
                            lastUpdatedTimeStamp = LocalDateTime.now()
                        )
                    }
                }
            }
        ).flow
    }

    suspend fun updateWatchStatus(
        item: WatchItem,
        newStatus: WatchStatus
    ) {
        commonRepository.updateWatchStatus(item, newStatus)
    }
}