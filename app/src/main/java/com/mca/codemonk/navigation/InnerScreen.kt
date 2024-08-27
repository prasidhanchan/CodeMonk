package com.mca.codemonk.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mca.home.navigation.homeNavigation
import com.mca.ui.component.CMBottomBar
import com.mca.ui.theme.Black
import com.mca.util.navigation.Route

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun NavGraphBuilder.innerScreen() {
    composable<Route.InnerScreen> {
        val navHostController = rememberNavController()

        val currentUser = FirebaseAuth.getInstance().currentUser

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { CMBottomBar(navHostController = navHostController) },
            containerColor = Black
        ) {
            NavHost(
                navController = navHostController,
                startDestination = Route.Home
            ) {
                homeNavigation(
                    navController = navHostController,
                    currentUserId = currentUser?.uid!!
                )
            }
        }
    }
}