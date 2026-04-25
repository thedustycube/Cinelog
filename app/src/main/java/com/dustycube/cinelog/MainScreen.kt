package com.dustycube.cinelog

import android.util.Log
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.ui.feature.details.MediaDetailsScreen
import com.dustycube.cinelog.ui.feature.details.SeasonDetailsScreen
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

    fun NavGraphBuilder.detailsScreen(navController: NavHostController) {
        composable(
            "${Routes.details}/{itemId}/{media_type}",
            arguments = listOf(
                navArgument("itemId") { type = NavType.IntType },
                navArgument("media_type") { type = NavType.StringType }
            )
        ) {
            MediaDetailsScreen(
                onSeasonCardClick = { showId, seasonNumber ->
                    navController.navigate("${Routes.seasonDetails}/$showId/$seasonNumber")
                }
            )
        }
        composable(
            "${Routes.seasonDetails}/{showId}/{season_number}",
            arguments = listOf(
                navArgument("showId") { type = NavType.IntType },
                navArgument("season_number") { type = NavType.IntType }
            )
        ) {
            SeasonDetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
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
            route = Routes.watchlistTab,
            rootRoute = Routes.watchlist,
            selectedIcon = Icons.Filled.Bookmarks,
            idleIcon = Icons.Outlined.Bookmarks
        ),
        NavItem(
            label = "Search",
            route = Routes.searchTab,
            rootRoute = Routes.search,
            selectedIcon = Icons.Filled.Search,
            idleIcon = Icons.Outlined.Search
        ),
        NavItem(
            label = "Genres",
            route = Routes.genreTab,
            rootRoute = Routes.genre,
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
                        onNavigateToTrendingTvShows = { navController.navigate(Routes.trendingTvShows) },
                        onCardClick = { item ->
                            val type = when (item) {
                                is Movie -> "movie"
                                is TvShow -> "tv"
                                else -> { item.media_type ?: "movie" }
                            }
                            navController.navigate("${Routes.details}/${item.id}/$type")
                        }
                    )
                }
                composable(
                    Routes.trendingMovies
                ) {
                    TrendingMoviesScreen(
                        onCardClick = { item ->
                            val type = "movie"
                            navController.navigate("${Routes.details}/${item.id}/$type")
                        }
                    )
                }
                composable(
                    Routes.trendingTvShows
                ) {
                    TrendingTvShowsScreen(
                        onCardClick = { item ->
                            val type = "tv"
                            navController.navigate("${Routes.details}/${item.id}/$type")
                        }
                    )
                }
                detailsScreen(navController)
            }
            navigation(startDestination = Routes.watchlist, route = Routes.watchlistTab) {
                composable(Routes.watchlist) {
                    WatchlistScreen(
                        onCardClick = { item ->
                            val type = when (item) {
                                is Movie -> "movie"
                                is TvShow -> "tv"
                                else -> item.media_type ?: "movie"
                            }
                            navController.navigate("${Routes.details}/${item.id}/$type")
                        }
                    )
                }
                detailsScreen(navController)
            }
            navigation(startDestination = Routes.search, route = Routes.searchTab) {
                composable(Routes.search) {
                    SearchScreen(
                        onCardClick = { item ->
                            val type = when (item) {
                                is Movie -> "movie"
                                is TvShow -> "tv"
                                else -> item.media_type ?: "movie"
                            }
                            navController.navigate("${Routes.details}/${item.id}/$type")
                        }
                    )
                }
                detailsScreen(navController)
            }
            navigation(startDestination = Routes.genre, route = Routes.genreTab) {
                composable(Routes.genre) {
                    GenreScreen(
                        onCardClick = { item ->
                            val type = when (item) {
                                is Movie -> "movie"
                                is TvShow -> "tv"
                                else -> item.media_type ?: "movie"
                            }
                            navController.navigate("${Routes.details}/${item.id}/$type")
                        }
                    )
                }
                detailsScreen(navController)
            }
            composable(
                Routes.settings
            ) {
                SettingsScreen()
            }
        }
    }
}