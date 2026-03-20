package com.dustycube.cinelog.ui.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.ui.component.BannerHeader
import com.dustycube.cinelog.ui.component.CardBuilder

@Composable
fun SearchScreen(
    onCardClick: (WatchItem) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
    ) {
        BannerHeader(
            "Search Items",
            hasIcon = false
        )
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("Search") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.search(text)
                    keyboardController?.hide()
                }),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResults.itemCount) { index ->
                val searchItem = searchResults[index]
                if (searchItem != null) {
                    CardBuilder(
                        item = searchItem,
                        onUpdateWatchStatus = viewModel::onUpdateWatchStatus,
                        isHorizontal = false,
                        onCardClick = onCardClick,
                        hasStatusBox = true
                    )
                }
            }
        }
    }
}