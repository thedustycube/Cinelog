package com.dustycube.cinelog

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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dustycube.cinelog.ui.feature.genre.GenreScreen
import com.dustycube.cinelog.ui.feature.home.HomeScreen
import com.dustycube.cinelog.ui.feature.home.TrendingMoviesScreen
import com.dustycube.cinelog.ui.feature.home.TrendingTvShowsScreen
import com.dustycube.cinelog.ui.feature.search.SearchScreen
import com.dustycube.cinelog.ui.feature.settings.SettingsScreen
import com.dustycube.cinelog.ui.feature.watchlist.WatchlistScreen
import com.dustycube.cinelog.ui.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainScreen(modifier: Modifier = Modifier) {



    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    fun navigateToTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navItems = listOf(
        NavItem(
            label = "Home",
            route = Routes.homeTab,
            rootRoute = Routes.home,
            selectedIcon = Icons.Filled.Home,
            idleIcon = Icons.Outlined.Home
        ),
        NavItem(
            label = "Watchlist",
            route = Routes.watchlist,
            rootRoute = "",
            selectedIcon = Icons.Filled.Bookmarks,
            idleIcon = Icons.Outlined.Bookmarks
        ),
        NavItem(
            label = "Search",
            route = Routes.search,
            rootRoute = "",
            selectedIcon = Icons.Filled.Search,
            idleIcon = Icons.Outlined.Search
        ),
        NavItem(
            label = "Genres",
            route = Routes.genre,
            rootRoute = "",
            selectedIcon = Icons.Filled.Category,
            idleIcon = Icons.Outlined.Category
        ),
        NavItem(
            label = "Settings",
            route = Routes.settings,
            rootRoute = "",
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
                    val isSelected = currentRoute?.hierarchy?.any { it.route == item.route } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                navController.navigate(item.rootRoute) {
                                    popUpTo(item.rootRoute) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            } else {
                                navigateToTab(item.route)
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
            startDestination = Routes.homeTab,
            modifier = modifier.padding(innerPadding)
        ) {
            navigation(
                startDestination = Routes.home,
                route = Routes.homeTab
            ) {
                composable(
                    Routes.home
                ) {
                    HomeScreen(
                        onNavigateToWatchlist = {
                            navigateToTab(Routes.watchlist)
                        },
                        onNavigateToTrendingMovies = { navController.navigate(Routes.trendingMovies) },
                        onNavigateToTrendingTvShows = { navController.navigate(Routes.trendingTvShows) }
                    )
                }
                composable(
                    Routes.trendingMovies
                ) {
                    TrendingMoviesScreen()
                }
                composable(
                    Routes.trendingTvShows
                ) {
                    TrendingTvShowsScreen()
                }
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
                Routes.genre
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

