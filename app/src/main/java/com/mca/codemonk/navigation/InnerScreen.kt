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

package com.mca.codemonk.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mca.home.navigation.homeNavigation
import com.mca.profile.navigation.aboutNavigation
import com.mca.profile.navigation.changePasswordNavigation
import com.mca.profile.navigation.editProfileNavigation
import com.mca.profile.navigation.profileNavigation
import com.mca.profile.screen.ProfileViewModel
import com.mca.ui.R
import com.mca.ui.component.CMAlertDialog
import com.mca.ui.component.CMBottomBar
import com.mca.ui.theme.Black
import com.mca.util.constants.getCurrentRoute
import com.mca.util.navigation.Route

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun NavGraphBuilder.innerScreen(
    viewModelProfile: ProfileViewModel,
    navigateToLogin: () -> Unit
) {
    composable<Route.InnerScreen> {
        val uiStateProfile by viewModelProfile.uiState.collectAsState()

        val navHostController = rememberNavController()

        val currentUser = FirebaseAuth.getInstance().currentUser

        var visible by remember { mutableStateOf(false) }
        var alertTitle by remember { mutableStateOf("") }
        var alertMessage by remember { mutableStateOf("") }

        val backStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.getCurrentRoute()
        val showBottomBar = when (currentRoute) {
            Route.Home -> true
            Route.LeaderBoard -> true
            Route.Notification -> true
            Route.Profile -> true
            else -> false
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Black,
            bottomBar = {
                CMBottomBar(
                    visible = showBottomBar,
                    isNewNotification = false,
                    navHostController = navHostController
                )
            }
        ) {
            NavHost(
                navController = navHostController,
                startDestination = Route.Home
            ) {
                homeNavigation(
                    navController = navHostController,
                    profileImage = uiStateProfile.currentUser.profileImage,
                    currentUserId = currentUser?.uid ?: ""
                )
                profileNavigation(
                    viewModel = viewModelProfile,
                    navHostController = navHostController,
                    onLogoutClick = { title, message ->
                        alertTitle = title
                        alertMessage = message
                        visible = true
                    }
                )
                editProfileNavigation(
                    viewModel = viewModelProfile,
                    navHostController = navHostController
                )
                changePasswordNavigation(
                    viewModel = viewModelProfile,
                    navHostController = navHostController
                )
                aboutNavigation(
                    viewModel = viewModelProfile,
                    navHostController = navHostController
                )
            }

            CMAlertDialog(
                title = alertTitle,
                message = alertMessage,
                visible = visible,
                confirmText = stringResource(id = R.string.confirm),
                dismissText = stringResource(id = R.string.cancel),
                onConfirm = {
                    viewModelProfile.logout()
                    navigateToLogin()
                },
                onDismiss = { visible = false }
            )
        }
    }
}