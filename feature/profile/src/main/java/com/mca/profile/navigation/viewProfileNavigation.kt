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
import androidx.navigation.toRoute
import com.mca.profile.screen.ProfileViewModel
import com.mca.profile.screen.ViewProfileScreen
import com.mca.util.navigation.Route

fun NavGraphBuilder.viewProfileNavigation(
    viewModel: ProfileViewModel,
    navHostController: NavHostController
) {
    composable<Route.ViewProfile>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 400))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 400))
        }
    ) { backStackEntry ->
        val username = backStackEntry.toRoute<Route.ViewProfile>().username
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            viewModel.getSelectedUser(
                username = username,
                onSuccess = { viewModel.getAllMentors() },
                onError = { navHostController.popBackStack() }
            )
        }

        ViewProfileScreen(
            uiState = uiState,
            onProfileCardClick = { usn ->
                navHostController.navigate(Route.ViewProfile(usn))
            },
            onBackClick = {
                navHostController.popBackStack()
                // Clear the selected user when navigating back to the home screen
                if (navHostController.previousBackStackEntry?.destination?.route == Route.Home.javaClass.simpleName) {
                    viewModel.clearSelectedUser()
                }
            }
        )
    }
}