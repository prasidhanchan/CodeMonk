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

package com.mca.notification.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mca.notification.screen.NotificationScreen
import com.mca.notification.screen.NotificationViewModel
import com.mca.util.navigation.Route

fun NavGraphBuilder.notificationNavigation(
    viewModel: NotificationViewModel,
    userType: String,
    navHostController: NavHostController,
    refreshUser: () -> Unit
) {
    composable<Route.Notification>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 400))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 400))
        }
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val lastSeen = System.currentTimeMillis()

        NotificationScreen(
            uiState = uiState,
            userType = userType,
            onSendNotificationClick = { navHostController.navigate(Route.SendNotification) },
            updateLastSeen = {
                viewModel.updateLastSeen(
                    lastSeen = lastSeen,
                    onSuccess = refreshUser
                )
            }
        )
    }
}