/*
 * Copyright © 2025 Prasidh Gopal Anchan
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

package com.mca.auth.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mca.auth.screen.AuthViewModel
import com.mca.auth.screen.ForgotPasswordScreen
import com.mca.util.constant.Constant.IN_OUT_DURATION
import com.mca.util.navigation.Route

fun NavGraphBuilder.forgotPasswordNavigation(
    viewModel: AuthViewModel,
    navController: NavController
) {
    composable<Route.ForgotPassword>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        }
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        ForgotPasswordScreen(
            uiState = uiState,
            onFindAccountClick = { viewModel.forgotPassword(uiState.email.trim()) },
            onEmailChange = viewModel::setEmail,
            onBackClick = { navController.popBackStack() }
        )
    }
}