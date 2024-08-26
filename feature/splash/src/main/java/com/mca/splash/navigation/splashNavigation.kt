package com.mca.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mca.splash.screen.SplashScreen
import com.mca.util.navigation.Route

fun NavGraphBuilder.splashNavigation(
    navController: NavController,
    isLoggedIn: Boolean,
) {
    composable<Route.Splash> {
        SplashScreen(
            isLoggedIn = isLoggedIn,
            navigateToLogin = { navController.navigate(Route.Login) },
            navigateToHome = { navController.navigate(Route.InnerScreen) }
        )
    }
}