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

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mca.profile.screen.EditProfileScreen
import com.mca.profile.screen.ProfileViewModel
import com.mca.ui.R
import com.mca.util.constants.LinkType
import com.mca.util.constants.SnackBarHelper.Companion.showSnackBar
import com.mca.util.constants.getLinkDetail
import com.mca.util.navigation.Route
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType

fun NavGraphBuilder.editProfileNavigation(
    viewModel: ProfileViewModel,
    navHostController: NavHostController
) {
    composable<Route.EditProfile> {
        val uiState by viewModel.uiState.collectAsState()
        val context = LocalContext.current

        EditProfileScreen(
            uiState = uiState,
            onUsernameChange = viewModel::setUsername,
            onNameChange = viewModel::setName,
            onProfileImageChange = viewModel::setProfileImage,
            onBioChange = viewModel::setBio,
            onCurrentProjectChange = viewModel::setCurrentProject,
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
                        viewModel.getUser()
                        navHostController.popBackStack()
                    }
                )
            },
            onUpdateClick = {
                viewModel.updateUser(
                    onSuccess = {
                        viewModel.getUser()
                        navHostController.popBackStack()
                    }
                )
            },
            onBackClick = {
                viewModel.getUser()
                navHostController.popBackStack()
            }
        )
    }
}