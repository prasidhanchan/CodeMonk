/*
 * Copyright © 2026 Prasidh Gopal Anchan
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

package com.mca.post.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.mca.post.screen.PostScreen
import com.mca.post.screen.PostViewModel
import com.mca.ui.R
import com.mca.util.constant.Constant.IN_OUT_DURATION
import com.mca.util.model.Post
import com.mca.util.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

fun NavGraphBuilder.postNavigation(
    userType: String,
    navHostController: NavHostController,
    sendPostNotification: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    composable<Route.Post>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        }
    ) { backStackEntry ->
        val viewModel: PostViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val scope = rememberCoroutineScope()

        val postId = backStackEntry.toRoute<Route.Post>().postId
        // If creating a new post, generate a new projectId
        val projectId = if (postId.isBlank()) {
            stringResource(R.string.my_post_id, Random.nextInt(0, Int.MAX_VALUE))
        } else {
            uiState.postId
        }

        LaunchedEffect(key1 = postId) {
            if (postId.isNotBlank()) viewModel.getPost(postId)
        }

        val post = Post(
            userId = uiState.userId.ifEmpty { currentUser?.uid!! }, // Empty if its a new post
            currentProject = uiState.currentProject.trim(),
            description = uiState.description.trim(),
            postId = projectId,
            teamMembers = uiState.teamMembers,
            projectProgress = uiState.projectProgress.toIntOrNull() ?: 0,
            deadline = uiState.deadline.trim(),
            likes = uiState.likes,
            timeStamp = if (uiState.timestamp == 0L) System.currentTimeMillis() else uiState.timestamp,
        )

        PostScreen(
            postId = postId,
            uiState = uiState,
            userType = userType,
            currentUserId = currentUser?.uid!!,
            onCurrentProjectChange = viewModel::setCurrentProject,
            onTeamMemberListChange = viewModel::setTeamMembers,
            onTeamMemberChange = { member ->
                scope.launch {
                    viewModel.getTags(member)
                    delay(2000L)
                }
            },
            onDescriptionChange = viewModel::setDescription,
            onProgressChange = viewModel::setProjectProgress,
            onDeadlineChange = viewModel::setDeadline,
            onPostClick = {
                viewModel.upsertPost(
                    post = post,
                    onSuccess = {
                        if (postId.isBlank()) sendPostNotification() // Only send notification if its a new post
                        navHostController.popBackStack()
                    },
                )
            },
            onBackClick = { navHostController.popBackStack() }
        )
    }
}