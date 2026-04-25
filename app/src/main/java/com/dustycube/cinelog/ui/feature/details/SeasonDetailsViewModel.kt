package com.dustycube.cinelog.ui.feature.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.model.Season
import com.dustycube.cinelog.data.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SeasonUiState(
    val isLoading: Boolean = true,
    val season: Season? = null,
    val error: String? = null
)

@HiltViewModel
class SeasonDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commonRepository: CommonRepository
) : ViewModel() {
    val showId: Int = checkNotNull(savedStateHandle["showId"])
    val seasonNumber: Int = checkNotNull(savedStateHandle["season_number"])

    private val _uiState = MutableStateFlow(SeasonUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchSeasonDetails()
    }

    private fun fetchSeasonDetails() {
        viewModelScope.launch {
            val result = commonRepository.getSeasonDetails(showId, seasonNumber)
            if (result != null) {
                _uiState.update { it.copy(isLoading = false, season = result) }
            } else {
                Log.e("SeasonDetailsViewModel", "Result: $result")
                _uiState.update { it.copy(isLoading = false, error = "Failed to load season data") }
            }
        }
    }
}