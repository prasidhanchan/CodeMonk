package com.mca.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mca.home.UiState
import com.mca.home.screen.HomeScreen
import com.mca.util.model.Post
import com.mca.util.navigation.Route

fun NavGraphBuilder.homeNavigation(
    navController: NavController,
    currentUserId: String
) {
    composable<Route.Home> {
        HomeScreen(
            uiState = UiState(
                posts = listOf(
                    Post(
                        userId = "1",
                        username = "kawaki_22",
                        userImage = "",
                        currentProject = "Project X",
                        teamMembers = listOf(
                            "me",
                            "naruto",
                            "uchiha_sasuke",
                            "kakashi"
                        ),
                        projectProgress = 0.2f,
                        deadline = "30 Nov 2024",
                        likes = listOf(
                            "naruto",
                            "kakashi",
                            "sasuke",
                            "minato",
                            "itachi"
                        ),
                        timeStamp = 1690885200000L
                    ),
                    Post(
                        userId = "2",
                        username = "kawaki_22",
                        userImage = "",
                        currentProject = "Project X",
                        teamMembers = listOf(
                            "me",
                            "rengoku",
                            "tomiyoka_giyu",
                            "akaza_cm"
                        ),
                        projectProgress = 0.2f,
                        deadline = "30 Nov 2024",
                        likes = listOf(
                            "naruto",
                            "kakashi",
                            "sasuke",
                            "minato",
                            "itachi"
                        ),
                        timeStamp = 1690885200022L
                    )
                )
            ),
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