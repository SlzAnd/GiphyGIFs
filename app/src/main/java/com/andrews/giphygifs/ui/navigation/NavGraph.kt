package com.andrews.giphygifs.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.andrews.giphygifs.ui.screen.details.DetailsScreen
import com.andrews.giphygifs.ui.screen.home.HomeScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(navController = navController)
        }

        composable<Screen.Details>(
            enterTransition = {
                slideInVertically (
                    animationSpec = tween(500),
                    initialOffsetY = { fullHeight -> fullHeight }
                )
            },
            exitTransition = {
                slideOutVertically (
                    animationSpec = tween(500),
                    targetOffsetY = { fullHeight -> fullHeight }
                )
            }
        ) {
            val index = it.toRoute<Screen.Details>().gifIndex
            DetailsScreen(gifIndex = index, navController = navController)
        }
    }
}