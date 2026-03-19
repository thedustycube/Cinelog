package com.dustycube.cinelog.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.dustycube.cinelog.data.model.SearchItem
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import com.dustycube.cinelog.data.repository.CommonRepository
import com.dustycube.cinelog.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    commonRepository: CommonRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val pagedResults = _searchQuery
        .filter { it.isNotEmpty() }
        .flatMapLatest { query ->
            searchRepository.getSearchPagingFlow(query)
        }
        .cachedIn(viewModelScope)

    private val fullWatchlist = commonRepository.getFullWatchlist()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: Flow<PagingData<SearchItem>> = combine(
        pagedResults,
        fullWatchlist
    ) { pagingData, watchlistItems ->
        pagingData.map { searchItem ->
            val savedItem = watchlistItems.find { it.id == searchItem.id }
            searchItem.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    fun search(query: String) {
         _searchQuery.value = query
    }

    fun onUpdateWatchStatus(item: WatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            searchRepository.updateWatchStatus(item, newStatus)
        }
    }
}