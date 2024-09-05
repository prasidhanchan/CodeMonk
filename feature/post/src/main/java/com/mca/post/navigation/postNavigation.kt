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

package com.mca.post.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.mca.post.screen.PostScreen
import com.mca.post.screen.PostViewModel
import com.mca.util.model.Post
import com.mca.util.navigation.Route

fun NavGraphBuilder.postNavigation(
    userType: String,
    username: String,
    userImage: String,
    navHostController: NavHostController
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    composable<Route.Post> {
        val viewModel: PostViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        val post = Post(
            currentProject = uiState.currentProject.trim(),
            userId = currentUser?.uid!!,
            username = username,
            userImage = userImage,
            teamMembers = uiState.teamMembers,
            projectProgress = uiState.projectProgress,
            deadline = uiState.deadline.trim(),
            likes = uiState.likes,
            timeStamp = System.currentTimeMillis()
        )

        PostScreen(
            uiState = uiState,
            userType = userType,
            onCurrentProjectChange = viewModel::setCurrentProject,
            onTeamMemberChange = viewModel::setTeamMembers,
            onProgressChange = viewModel::setProjectProgress,
            onDeadlineChange = viewModel::setDeadline,
            onPostClick = {
                viewModel.upsertPost(
                    post = post,
                    onSuccess = { navHostController.popBackStack() },
                )
            },
            onBackClick = { navHostController.popBackStack() }
        )
    }
}