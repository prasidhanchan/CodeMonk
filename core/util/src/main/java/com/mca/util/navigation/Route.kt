/*
 * Copyright © 2025 Prasidh Gopal Anchan
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

package com.mca.util.navigation

import com.mca.util.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val icon: Int = 0, val newNotificationIcon: Int = 0) {

    @Serializable
    data object Splash : Route()

    @Serializable
    data object SignUp : Route()

    @Serializable
    data object Login : Route()

    @Serializable
    data object ForgotPassword : Route()

    @Serializable
    data object InnerScreen : Route()

    @Serializable
    data object Home : Route(icon = R.drawable.ic_home)

    @Serializable
    data object LeaderBoard : Route(icon = R.drawable.ic_leaderboard)

    @Serializable
    data object Notification :
        Route(
            icon = R.drawable.ic_notification,
            newNotificationIcon = R.drawable.ic_new_notification
        )

    @Serializable
    data object Profile : Route(icon = R.drawable.ic_profile)

    @Serializable
    data object EditProfile : Route()

    @Serializable
    data object ChangePassword : Route()

    @Serializable
    data class Post(val postId: String = "") : Route(icon = R.drawable.ic_add)

    @Serializable
    data object Announcement : Route()

    @Serializable
    data class ViewProfile(val userId: String) : Route()

    @Serializable
    data object About : Route()

    @Serializable
    data object Search : Route()

    @Serializable
    data object SendNotification : Route()

    companion object {
        val routes = listOf(
            Home,
            LeaderBoard,
            Post(),
            Notification,
            Profile
        )
    }
}