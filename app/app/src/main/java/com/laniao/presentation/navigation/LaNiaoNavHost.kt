package com.laniao.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.laniao.R
import com.laniao.presentation.screens.AddEntryScreen
import com.laniao.presentation.screens.calendar.CalendarScreen
import com.laniao.presentation.screens.home.HomeScreen
import com.laniao.presentation.screens.schedule.ScheduleScreen
import com.laniao.presentation.screens.settings.SettingsScreen
import com.laniao.presentation.screens.statistics.StatisticsScreen

data class BottomNavItem(
    val route: NavRoutes,
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val contentDescription: String
)

@Composable
fun LaNiaoNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    fun navigateToTopLevel(route: NavRoutes, selected: Boolean) {
        if (selected) {
            // Single tap on selected tab resets that tab to its root state.
            currentDestination?.id?.let { destinationId ->
                navController.popBackStack(destinationId, inclusive = true)
            }
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = false
            }
        } else {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem(
            route = NavRoutes.Home,
            labelResId = R.string.nav_home,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            contentDescription = "Home"
        ),
        BottomNavItem(
            route = NavRoutes.Schedule,
            labelResId = R.string.nav_schedule,
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Outlined.List,
            contentDescription = "Schedule"
        ),
        BottomNavItem(
            route = NavRoutes.Calendar,
            labelResId = R.string.nav_calendar,
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            contentDescription = "Calendar"
        ),
        BottomNavItem(
            route = NavRoutes.Statistics,
            labelResId = R.string.nav_statistics,
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            contentDescription = "Statistics"
        ),
        BottomNavItem(
            route = NavRoutes.Settings,
            labelResId = R.string.nav_settings,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            contentDescription = "Settings"
        )
    )

    // Determine if bottom bar should be shown
    val showBottomBar = currentDestination?.hierarchy?.any { dest ->
        bottomNavItems.any { item -> dest.hasRoute(item.route::class) }
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { 
                            it.hasRoute(item.route::class) 
                        } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.contentDescription
                                )
                            },
                            label = { Text(stringResource(item.labelResId), style = MaterialTheme.typography.labelSmall, maxLines = 1) },
                            selected = selected,
                            onClick = {
                                navigateToTopLevel(item.route, selected)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(250)) },
            exitTransition = { fadeOut(animationSpec = tween(250)) },
            popEnterTransition = { fadeIn(animationSpec = tween(250)) },
            popExitTransition = { fadeOut(animationSpec = tween(250)) }
        ) {
            composable<NavRoutes.Home> {
                HomeScreen(
                    onNavigateToAddEntry = { entryId ->
                        navController.navigate(NavRoutes.AddEntry(entryId))
                    },
                    onNavigateToSchedule = {
                        navigateToTopLevel(NavRoutes.Schedule, selected = false)
                    },
                    onNavigateToCalendar = {
                        navigateToTopLevel(NavRoutes.Calendar, selected = false)
                    },
                    onNavigateToSettings = {
                        navigateToTopLevel(NavRoutes.Settings, selected = false)
                    }
                )
            }

            composable<NavRoutes.Calendar> {
                CalendarScreen(
                    onNavigateToEntry = { entryId ->
                        navController.navigate(NavRoutes.AddEntry(entryId))
                    }
                )
            }

            composable<NavRoutes.Statistics> {
                StatisticsScreen()
            }

            composable<NavRoutes.Settings> {
                SettingsScreen()
            }

            composable<NavRoutes.Schedule> {
                ScheduleScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<NavRoutes.AddEntry> {
                AddEntryScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

