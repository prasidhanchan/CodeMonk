package com.mca.auth.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mca.auth.screen.AuthViewModel
import com.mca.auth.screen.ForgotPasswordScreen
import com.mca.util.navigation.Route

fun NavGraphBuilder.forgotPasswordNavigation(
    viewModel: AuthViewModel,
    navController: NavController
) {
    composable<Route.ForgotPassword> {
        val uiState by viewModel.uiState.collectAsState()

        ForgotPasswordScreen(
            uiState = uiState,
            onFindAccountClick = { viewModel.forgotPassword(uiState.email) },
            onEmailChange = viewModel::setEmail,
            onBackClick = { navController.popBackStack() }
        )
    }
}