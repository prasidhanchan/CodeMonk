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