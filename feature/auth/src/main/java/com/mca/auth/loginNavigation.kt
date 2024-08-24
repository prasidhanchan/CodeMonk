package com.mca.auth

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
                    password = uiState.password,
                    onSuccess = { }
                )
            },
            onForgotPasswordClick = { }
        )
    }
}