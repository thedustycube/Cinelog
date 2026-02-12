package com.cubedusty.cinelog

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val idleIcon: ImageVector
)