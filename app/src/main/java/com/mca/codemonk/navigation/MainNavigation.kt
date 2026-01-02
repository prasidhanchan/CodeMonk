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

package com.mca.codemonk.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mca.auth.navigation.forgotPasswordNavigation
import com.mca.auth.navigation.loginNavigation
import com.mca.auth.navigation.signUpNavigation
import com.mca.auth.screen.AuthViewModel
import com.mca.splash.navigation.splashNavigation
import com.mca.ui.component.CMSnackBar
import com.mca.ui.theme.Black
import com.mca.ui.theme.Green
import com.mca.ui.theme.Red
import com.mca.util.constant.SnackBarHelper.Companion.messageState
import com.mca.util.constant.SnackBarHelper.Companion.resetMessageState
import com.mca.util.navigation.Route
import com.mca.util.warpper.ResponseType

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigation(
    viewModelAuth: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val currentUser = FirebaseAuth.getInstance().currentUser
    val snackBarResponse by messageState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            CMSnackBar(
                message = snackBarResponse.message,
                visible = snackBarResponse.message?.isNotBlank() == true,
                iconColor = if (snackBarResponse.responseType == ResponseType.ERROR) Red else Green,
                onFinish = { resetMessageState() }
            )
        },
        containerColor = Black
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Splash,
            modifier = Modifier.systemBarsPadding()
        ) {
            splashNavigation(
                navController = navController,
                isLoggedIn = currentUser != null
            )
            signUpNavigation(
                viewModel = viewModelAuth,
                navController = navController
            )
            loginNavigation(
                viewModel = viewModelAuth,
                navController = navController
            )
            forgotPasswordNavigation(
                viewModel = viewModelAuth,
                navController = navController
            )
            innerScreen(
                navigateToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.InnerScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}