package com.mca.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mca.home.UiState
import com.mca.home.screen.HomeScreen
import com.mca.util.navigation.Route

fun NavGraphBuilder.homeNavigation(
    navController: NavController,
    currentUserId: String
) {
    composable<Route.Home> {
        HomeScreen(
            uiState = UiState(),
            isVerified = { true },
            currentUserId = currentUserId,
            onProfileClick = { },
            onSearchClick = { },
            onUsernameClick = { },
            onLikeClick = { },
            onUnlikeClick = { },
            onDeletedClick = { }
        )
    }
}