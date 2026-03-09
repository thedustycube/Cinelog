package com.dustycube.cinelog.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.models.SearchItem
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import com.dustycube.cinelog.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<SearchItem>>(emptyList())
    val searchResults: StateFlow<List<SearchItem>> = _searchResults.asStateFlow()

    fun search(query: String) {
         viewModelScope.launch {
             searchRepository.getSearchResultsWithStatus(query)
                 .collect { results ->
                     _searchResults.value = results
                 }
         }
    }

    fun onUpdateWatchStatus(item: UserWatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            searchRepository.updateWatchStatus(item, newStatus)
        }
    }
}