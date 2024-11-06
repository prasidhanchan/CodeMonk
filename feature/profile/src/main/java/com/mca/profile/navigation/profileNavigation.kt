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

package com.mca.profile.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mca.profile.screen.ProfileScreen
import com.mca.profile.screen.ProfileViewModel
import com.mca.util.constant.Constant.IN_OUT_DURATION
import com.mca.util.navigation.Route

fun NavGraphBuilder.profileNavigation(
    viewModel: ProfileViewModel,
    navHostController: NavHostController,
    onLogoutClick: () -> Unit
) {
    composable<Route.Profile>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        }
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = uiState.currentUser) {
            if (uiState.currentUser.userId.isBlank()) viewModel.getCurrentUser()
        }

        ProfileScreen(
            uiState = uiState,
            onEditProfileClick = { navHostController.navigate(Route.EditProfile) },
            onChangePasswordClick = { navHostController.navigate(Route.ChangePassword) },
            onAboutClick = { navHostController.navigate(Route.About) },
            onLogoutClick = onLogoutClick
        )
    }
}