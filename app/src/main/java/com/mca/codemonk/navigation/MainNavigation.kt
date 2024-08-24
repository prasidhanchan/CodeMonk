package com.mca.codemonk.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mca.auth.AuthViewModel
import com.mca.auth.loginNavigation
import com.mca.splash.splashNavigation
import com.mca.util.navigation.Route

@Composable
fun MainNavigation(
    viewModelAuth: AuthViewModel = hiltViewModel()
) {

    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser

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
    }
}