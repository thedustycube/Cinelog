package com.dustycube.cinelog.ui.features.search

import androidx.lifecycle.ViewModel
import com.dustycube.cinelog.di.RepositoryModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val remoteRepository: RepositoryModule
) : ViewModel() {

}