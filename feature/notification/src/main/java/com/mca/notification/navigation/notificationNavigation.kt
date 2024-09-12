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

package com.mca.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mca.notification.UiState
import com.mca.notification.screen.NotificationScreen
import com.mca.util.model.Notification
import com.mca.util.navigation.Route

fun NavGraphBuilder.notificationNavigation(
    navHostController: NavHostController
) {
    composable<Route.Notification> {
        NotificationScreen(
            uiState = UiState(
                notifications = listOf(
                    Notification(
                        id = "1",
                        title = "New event",
                        body = "There will be a hackathon this saturday, interested can register @codemonk.club",
                        timeStamp = 1726143652651L
                    ),
                    Notification(
                        id = "2",
                        title = "Laptop remainder",
                        body = "Don't forget to bring your laptop this saturday",
                        timeStamp = 1694092800000L
                    )
                )
            )
        )
    }
}