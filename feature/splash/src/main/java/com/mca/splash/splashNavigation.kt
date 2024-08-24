package com.mca.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mca.util.navigation.Route

fun NavGraphBuilder.splashNavigation(
    navController: NavController,
    isLoggedIn: Boolean,
) {
    composable<Route.Splash> {
        SplashScreen(
            isLoggedIn = isLoggedIn,
            navigateToLogin = { navController.navigate(Route.Login) },
            navigateToHome = { }
        )
    }
}