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

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.mca.post.screen.AnnouncementScreen
import com.mca.post.screen.PostViewModel
import com.mca.ui.R
import com.mca.util.constant.Constant.IN_OUT_DURATION
import com.mca.util.constant.PostType
import com.mca.util.model.Post
import com.mca.util.navigation.Route
import kotlin.random.Random

fun NavGraphBuilder.announcementNavigation(
    navHostController: NavHostController,
    sendNotification: () -> Unit,
    updatePoints: () -> Unit
) {
    composable<Route.Announcement>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        }
    ) {
        val viewModel: PostViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val currentUser = FirebaseAuth.getInstance().currentUser

        val context = LocalContext.current

        AnnouncementScreen(
            uiState = uiState,
            onDescriptionChange = viewModel::setDescription,
            onPostClick = { images ->
                viewModel.addAnnouncement(
                    post = Post(
                        userId = currentUser?.uid!!,
                        postType = PostType.ANNOUNCEMENT.name,
                        postId = context.getString(
                            R.string.my_post_id,
                            Random.nextInt(0, Int.MAX_VALUE)
                        ),
                        description = uiState.description,
                        images = images,
                        timeStamp = System.currentTimeMillis()
                    ),
                    context = context,
                    onSuccess = {
                        sendNotification()
                        navHostController.popBackStack()
                        updatePoints()
                    }
                )
            },
            onBackClick = { navHostController.popBackStack() }
        )
    }
}