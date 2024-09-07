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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mca.profile.screen.ChangePasswordScreen
import com.mca.profile.screen.ProfileViewModel
import com.mca.ui.R
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.navigation.Route
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType

fun NavGraphBuilder.changePasswordNavigation(
    viewModel: ProfileViewModel,
    navHostController: NavHostController
) {
    composable<Route.ChangePassword>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 200))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 200))
        }
    ) {
        val context = LocalContext.current

        val uiState by viewModel.uiState.collectAsState()

        ChangePasswordScreen(
            uiState = uiState,
            onPasswordChange = viewModel::setPassword,
            onChangePasswordClick = {
                viewModel.changePassword(
                    password = uiState.newPassword,
                    onSuccess = {
                        navHostController.popBackStack()
                        viewModel.clearPassword()
                        showSnackBar(
                            response = Response(
                                message = context.getString(R.string.password_updated),
                                responseType = ResponseType.SUCCESS
                            )
                        )
                    }
                )
            },
            onBackClick = {
                navHostController.popBackStack()
                viewModel.clearPassword()
            }
        )
    }
}