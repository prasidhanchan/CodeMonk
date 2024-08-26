package com.mca.codemonk.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mca.auth.navigation.forgotPasswordNavigation
import com.mca.auth.navigation.loginNavigation
import com.mca.auth.screen.AuthViewModel
import com.mca.splash.navigation.splashNavigation
import com.mca.ui.component.CMSnackBar
import com.mca.ui.theme.Black
import com.mca.ui.theme.Green
import com.mca.ui.theme.Red
import com.mca.util.navigation.Route
import com.mca.util.warpper.ResponseType

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigation(
    viewModelAuth: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uiStateAuth by viewModelAuth.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            CMSnackBar(
                message = uiStateAuth.response?.message,
                visible = uiStateAuth.response != null,
                iconColor = if (uiStateAuth.response?.responseType == ResponseType.ERROR) Red else Green,
                onFinish = { viewModelAuth.clearMessage() }
            )
        },
        containerColor = Black
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Splash
        ) {
            splashNavigation(
                isLoggedIn = currentUser != null,
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
            innerScreen()
        }
    }
}