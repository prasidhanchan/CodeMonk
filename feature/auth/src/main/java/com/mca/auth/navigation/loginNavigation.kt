package com.mca.auth.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mca.auth.screen.AuthViewModel
import com.mca.auth.screen.LoginScreen
import com.mca.util.navigation.Route

fun NavGraphBuilder.loginNavigation(
    viewModel: AuthViewModel,
    navController: NavController
) {

    composable<Route.Login> {
        val uiState by viewModel.uiState.collectAsState()

        LoginScreen(
            uiState = uiState,
            onEmailChange = viewModel::setEmail,
            onPasswordChange = viewModel::setPassword,
            onLoginClick = {
                viewModel.login(
                    email = uiState.email,
                    password = uiState.password
                )
            },
            onForgotPasswordClick = { navController.navigate(Route.ForgotPassword) }
        )
    }
}