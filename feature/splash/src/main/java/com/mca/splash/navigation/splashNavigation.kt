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

package com.mca.splash.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mca.splash.screen.SplashScreen
import com.mca.util.navigation.Route

fun NavGraphBuilder.splashNavigation(
    navController: NavController,
    isLoggedIn: Boolean
) {
    composable<Route.Splash>(
        enterTransition = { EnterTransition.None },
        exitTransition = {
            fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
        }
    ) {
        SplashScreen(
            isLoggedIn = isLoggedIn,
            navigateToLogin = {
                navController.popBackStack()
                navController.navigate(Route.Login)
            },
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Route.InnerScreen)
            }
        )
    }
}