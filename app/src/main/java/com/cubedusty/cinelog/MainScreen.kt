package com.cubedusty.cinelog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cubedusty.cinelog.ui.navigation.Routes
import com.cubedusty.cinelog.screens.GenreScreen
import com.cubedusty.cinelog.ui.home.HomeScreen
import com.cubedusty.cinelog.screens.SearchScreen
import com.cubedusty.cinelog.screens.SettingsScreen
import com.cubedusty.cinelog.ui.home.HomeViewModel
import com.cubedusty.cinelog.ui.watchlist.WatchlistScreen

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = listOf(
        NavItem(
            label = "Home",
            route = Routes.home,
            selectedIcon = Icons.Filled.Home,
            idleIcon = Icons.Outlined.Home
        ),
        NavItem(
            label = "Watchlist",
            route = Routes.watchlist,
            selectedIcon = Icons.Filled.Bookmarks,
            idleIcon = Icons.Outlined.Bookmarks
        ),
        NavItem(
            label = "Search",
            route = Routes.search,
            selectedIcon = Icons.Filled.Search,
            idleIcon = Icons.Outlined.Search
        ),
        NavItem(
            label = "Genres",
            route = Routes.genres,
            selectedIcon = Icons.Filled.Category,
            idleIcon = Icons.Outlined.Category
        ),
        NavItem(
            label = "Settings",
            route = Routes.settings,
            selectedIcon = Icons.Filled.Settings,
            idleIcon = Icons.Outlined.Settings
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color.Gray),
                title = {
                    Text(
                        text = "Cinelog",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Gray
            ) {
                navItems.forEach { item ->
                    val isSelected = currentRoute == item.route

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(text = item.label) },
                        alwaysShowLabel = false,
                        icon = {
                            Icon(
                                imageVector = if(isSelected){
                                    item.selectedIcon
                                } else {
                                    item.idleIcon
                                },
                                contentDescription = item.label
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.home,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(
                Routes.home
            ) {
                HomeScreen()
            }
            composable(
                Routes.watchlist
            ) {
                WatchlistScreen()
            }
            composable(
                Routes.search
            ) {
                SearchScreen()
            }
            composable(
                Routes.genres
            ) {
                GenreScreen()
            }
            composable(
                Routes.settings
            ) {
                SettingsScreen()
            }
        }
    }
}

