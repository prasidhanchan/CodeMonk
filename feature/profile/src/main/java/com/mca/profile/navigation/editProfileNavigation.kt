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

package com.mca.profile.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mca.profile.screen.EditProfileScreen
import com.mca.profile.screen.ProfileViewModel
import com.mca.ui.R
import com.mca.util.constant.Constant.IN_OUT_DURATION
import com.mca.util.constant.LinkType
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.constant.getLinkDetail
import com.mca.util.navigation.Route
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun NavGraphBuilder.editProfileNavigation(
    viewModel: ProfileViewModel,
    navHostController: NavHostController
) {
    composable<Route.EditProfile>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        }
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        EditProfileScreen(
            uiState = uiState,
            onUsernameChange = viewModel::setUsername,
            onNameChange = viewModel::setName,
            onProfileImageChange = viewModel::setProfileImage,
            onBioChange = viewModel::setBio,
            onCurrentProjectChange = viewModel::setCurrentProject,
            onMentorChange = { mentor ->
                scope.launch {
                    viewModel.setMentor(mentor)
                    delay(1000L)
                    viewModel.getMentorTags(mentor)
                }
            },
            setMentor = { mentor ->
                viewModel.setMentor(mentor)
                viewModel.getMentorTags(username = "") // Clear searched tags
            },
            onAddLinkCLick = { link ->
                if (link.startsWith("http://") || link.startsWith("https://")) {
                    when (link.getLinkDetail()) {
                        LinkType.GITHUB -> viewModel.setGitHubLink(link)

                        LinkType.LINKEDIN -> viewModel.setLinkedInLink(link)

                        else -> viewModel.setPortfolioLink(link)
                    }
                } else {
                    showSnackBar(
                        response = Response(
                            message = context.getString(R.string.invalid_link),
                            responseType = ResponseType.ERROR
                        )
                    )
                }
            },
            onRemoveLinkCLick = { linkType ->
                when (linkType) {
                    LinkType.LINKEDIN -> viewModel.setLinkedInLink("")
                    LinkType.GITHUB -> viewModel.setGitHubLink("")
                    LinkType.MORE -> viewModel.setPortfolioLink("")
                }
            },
            onRemoveProfileImage = {
                viewModel.removeProfileImage(
                    onSuccess = {
                        viewModel.getCurrentUser()
                        navHostController.popBackStack()
                        viewModel.setProfileImage("")
                    }
                )
            },
            onUpdateClick = {
                viewModel.updateUser(
                    onSuccess = {
                        viewModel.getCurrentUser()
                        navHostController.popBackStack()
                    }
                )
            },
            onBackClick = {
                navHostController.popBackStack()
                viewModel.getCurrentUser() // Reset state for all fields if not updated
                viewModel.getMentorTags(username = "") // Clear searched tags
            }
        )
    }
}