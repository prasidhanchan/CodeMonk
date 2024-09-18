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

package com.mca.home.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mca.home.screen.HomeScreen
import com.mca.home.screen.HomeViewModel
import com.mca.ui.R
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.navigation.Route
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType

fun NavGraphBuilder.homeNavigation(
    viewModel: HomeViewModel,
    navHostController: NavHostController,
    profileImage: String,
    currentUserId: String,
    currentUsername: String,
    currentUserType: String,
    onDeletedClick: (postId: String) -> Unit,
    sendLikeNotification: (token: String) -> Unit
) {
    composable<Route.Home>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 400))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 400))
        }
    ) {
        val uiState by viewModel.uiState.collectAsState()

        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { result ->
                if (!result) {
                    showSnackBar(
                        response = Response(
                            message = context.getString(R.string.notification_permission_denied),
                            responseType = ResponseType.ERROR
                        )
                    )
                }
            }
        )

        LaunchedEffect(key1 = Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_DENIED &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        HomeScreen(
            uiState = uiState,
            profileImage = profileImage,
            currentUserId = currentUserId,
            currentUsername = currentUsername,
            currentUserType = currentUserType,
            onProfileClick = { navHostController.navigate(Route.EditProfile) },
            onSearchClick = { navHostController.navigate(Route.Search) },
            onUsernameClick = { userId ->
                navHostController.navigate(Route.ViewProfile(userId))
            },
            onEditPostClick = { postId ->
                navHostController.navigate(Route.Post(postId))
            },
            onLikeClick = { postId, token ->
                viewModel.like(
                    postId = postId,
                    currentUsername = currentUsername,
                    onSuccess = { sendLikeNotification(token) }
                )
            },
            onUnlikeClick = { postId ->
                viewModel.unLike(
                    postId = postId,
                    currentUsername = currentUsername
                )
            },
            onDeletedClick = onDeletedClick
        )
    }
}