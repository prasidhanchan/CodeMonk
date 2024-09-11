/*
 * Copyright © 2024 Prasidh Gopal Anchan
 *
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://creativecommons.org/licenses/by-nc-nd/4.0/
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.mca.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.home.UiState
import com.mca.home.component.HomeAppBar
import com.mca.home.component.Post
import com.mca.ui.R
import com.mca.ui.component.CMAlertDialog
import com.mca.ui.component.EmptyResponseIndicator
import com.mca.ui.theme.Black
import com.mca.util.model.Post
import com.mca.util.model.User

@Composable
internal fun HomeScreen(
    uiState: UiState,
    profileImage: String,
    currentUserId: String,
    currentUsername: String,
    currentUserType: String,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onUsernameClick: (String) -> Unit,
    onEditPostClick: (postId: String) -> Unit,
    onLikeClick: (postId: String) -> Unit,
    onUnlikeClick: (postId: String) -> Unit,
    onDeletedClick: (postId: String) -> Unit
) {
    val state = rememberLazyListState()

    var visible by remember { mutableStateOf(false) }
    var postId by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Post(
                posts = { uiState.posts },
                currentUserId = currentUserId,
                currentUsername = currentUsername,
                currentUserType = currentUserType,
                loading = uiState.loading,
                state = state,
                onLikeClick = onLikeClick,
                onUnlikeClick = onUnlikeClick,
                onUsernameClick = onUsernameClick,
                onEditPostClick = onEditPostClick,
                onDeleteClick = { id ->
                    postId = id
                    visible = true
                },
                appBar = {
                    HomeAppBar(
                        userImage = profileImage,
                        onSearchClick = onSearchClick,
                        onProfileClick = onProfileClick
                    )
                }
            )
        }

        CMAlertDialog(
            title = stringResource(id = R.string.delete_post),
            message = stringResource(R.string.delete_post_message),
            visible = visible,
            confirmText = stringResource(id = R.string.delete),
            dismissText = stringResource(id = R.string.cancel),
            onConfirm = {
                visible = false
                onDeletedClick(postId)
            },
            onDismiss = { visible = false }
        )

        EmptyResponseIndicator(
            visible = uiState.posts.isEmpty() && !uiState.loading,
            message = stringResource(id = R.string.no_posts_yet)
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        uiState = UiState(
            posts = listOf(
                Pair(
                    Post(
                        userId = "1",
                        currentProject = "Project X",
                        teamMembers = listOf(
                            "me",
                            "naruto",
                            "uchiha_sasuke",
                            "kakashi"
                        ),
                        projectProgress = 20,
                        deadline = "30 Nov, 2024",
                        likes = listOf(
                            "naruto",
                            "kakashi",
                            "sasuke",
                            "minato",
                            "itachi"
                        ),
                        timeStamp = 1690885200000L
                    ),
                    User(
                        userId = "1",
                        username = "kawaki_22",
                        profileImage = "",
                        isVerified = true
                    )
                )
            )
        ),
        profileImage = "",
        currentUserId = "1",
        currentUsername = "pra_sidh_22",
        currentUserType = "student",
        onProfileClick = { },
        onSearchClick = { },
        onUsernameClick = { },
        onEditPostClick = { },
        onLikeClick = { },
        onUnlikeClick = { },
        onDeletedClick = { }
    )
}