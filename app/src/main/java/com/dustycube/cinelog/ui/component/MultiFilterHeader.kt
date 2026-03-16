package com.dustycube.cinelog.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dustycube.cinelog.data.model.WatchStatus

@Composable
fun MultiFilterHeader(
    selectedStatuses: Set<WatchStatus>,
    onToggleStatus: (WatchStatus) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val displayStatuses = WatchStatus.entries.filter { it != WatchStatus.NONE }
            .sortedByDescending { it.order }

        items(displayStatuses) { status ->
            val isSelected = selectedStatuses.contains(status)

            FilterChip(
                selected = isSelected,
                onClick = { onToggleStatus(status) },
                label = { Text(status.name.lowercase().replaceFirstChar { it.uppercase() }) },
                leadingIcon = {
                    Icon(
                        imageVector = status.icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
    }
}