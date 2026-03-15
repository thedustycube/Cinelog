package com.dustycube.cinelog.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.models.SearchItem
import com.dustycube.cinelog.data.models.WatchStatus
import com.dustycube.cinelog.data.repository.CommonRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

class SearchPagingSource(
    private val api: TMDBApiService,
    private val query: String,
    commonRepository: CommonRepository
) : PagingSource<Int, SearchItem>() {
    val accessToken = commonRepository.fetchAccessToken()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        val position = params.key ?: 1
        return try {
            val response = api.getSearchResults(query = query, page = position, token = accessToken)

            val searchItems = response.results.map { searchItem ->
                searchItem.copy(
                    watchStatus = WatchStatus.NONE,
                    lastUpdatedTimeStamp = LocalDateTime.now()
                )
            }

            LoadResult.Page(
                data = searchItems,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position >= 5 || position >= response.total_pages) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}