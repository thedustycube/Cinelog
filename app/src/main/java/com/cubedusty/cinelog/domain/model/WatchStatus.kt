package com.cubedusty.cinelog.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.outlined.ArrowDropDownCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.ui.graphics.vector.ImageVector

enum class WatchStatus(val order: Int, val icon: ImageVector) {
    NONE(6, Icons.Outlined.BookmarkBorder),
    WATCHING(5, Icons.Default.RemoveRedEye),
    REWATCHING(4, Icons.Default.FastRewind),
    COMPLETED(3, Icons.Default.CheckCircle),
    PLANNING(2, Icons.Default.BookmarkAdded),
    PAUSED(1, Icons.Default.Pause),
    DROPPED(0, Icons.Outlined.ArrowDropDownCircle)
}