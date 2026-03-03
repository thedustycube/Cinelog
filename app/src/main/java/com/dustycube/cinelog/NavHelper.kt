package com.dustycube.cinelog

import androidx.navigation.NavController

fun NavController.switchTab(route: String) {
    this.navigate(route) {
        popUpTo(this@switchTab.graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}