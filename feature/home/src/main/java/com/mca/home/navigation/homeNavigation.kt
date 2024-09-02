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

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
    composable<Route.Home>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 400))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 400))
        }
    ) {
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