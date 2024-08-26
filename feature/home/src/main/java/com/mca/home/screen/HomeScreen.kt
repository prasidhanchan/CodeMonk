package com.mca.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.home.UiState
import com.mca.home.component.HomeAppBar
import com.mca.home.component.Post
import com.mca.util.model.Post

@Composable
internal fun HomeScreen(
    uiState: UiState,
    isVerified: (String) -> Boolean,
    currentUserId: String,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onUsernameClick: (String) -> Unit,
    onLikeClick: () -> Unit,
    onUnlikeClick: () -> Unit,
    onDeletedClick: (postId: String) -> Unit
) {
    val state = rememberLazyListState()

    Column(
        modifier = Modifier
            .padding(all = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Post(
            posts = uiState.posts,
            isVerified = isVerified,
            currentUserId = currentUserId,
            loading = uiState.loading,
            state = state,
            onLikeClick = onLikeClick,
            onUnlikeClick = onUnlikeClick,
            onUsernameClick = onUsernameClick,
            onDeleteClick = onDeletedClick,
            appBar = {
                HomeAppBar(
                    userImage = uiState.profileImage,
                    onSearchClick = onSearchClick,
                    onProfileClick = onProfileClick
                )
            }
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
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
                    deadline = "30 Nov, 2024",
                    likes = listOf(
                        "naruto",
                        "kakashi",
                        "sasuke",
                        "minato",
                        "itachi"
                    ),
                    timeStamp = 1690885200000L
                )
            )
        ),
        isVerified = { true },
        currentUserId = "1",
        onProfileClick = { },
        onSearchClick = { },
        onUsernameClick = { },
        onLikeClick = { },
        onUnlikeClick = { },
        onDeletedClick = { }
    )
}