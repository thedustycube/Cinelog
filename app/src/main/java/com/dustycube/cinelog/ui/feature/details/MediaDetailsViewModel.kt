package com.dustycube.cinelog.ui.feature.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    private val mediaType: String = checkNotNull(savedStateHandle["media_type"])

    val uiState: StateFlow<DetailUiState> = flow {
        emit(DetailUiState.Loading)
        try {
            val item = commonRepository.getItemById(itemId, mediaType)
            emit(DetailUiState.Success(item))
        } catch (e: Exception) {
            emit(DetailUiState.Error(e.message ?: "Something went wrong"))
            Log.e("MediaDetailsViewModel: failed to get the item. Error: ", e.toString())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailUiState.Loading)
}

sealed class DetailUiState {
    object Loading: DetailUiState()
    data class Success(val item: WatchItem?) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}