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

package com.mca.util.constant

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.navigation.NavBackStackEntry
import com.mca.util.model.Post
import com.mca.util.model.User
import com.mca.util.navigation.Route
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class LinkType {
    GITHUB,
    LINKEDIN,
    MORE
}

/**
 * Function to converts a list of likes to a string of liked by.
 * Ex: Liked by kawaki_22, naruto and 2 others
 */
fun List<String>.toLikedBy(): String {
    return when (this.size) {
        1 -> "Liked by ${this[0]}"
        2 -> "Liked by ${this[0]} and ${this[1]}"
        3 -> "Liked by ${this[0]}, ${this[1]} and 1 other"
        else -> "Liked by ${this[0]}, ${this[1]} and ${(this.size - 2)} others"
    }
}

/**
 * Function to convert a Long timestamp to a readable date.
 * Ex: 12:00 PM Thu, 1 Aug 2024
 */
fun Long.toTimeStamp(): String {
    return SimpleDateFormat(
        "hh:mm a EEE, d MMM yyyy",
        Locale.getDefault()
    ).format(Date(this))
}

/**
 * Function to get the currently selected route.
 * @return Returns [Route] of the selected type.
 */
fun NavBackStackEntry.getCurrentRoute(): Route {
    var screen: Route = Route.Home

    this.destination.route
        ?.substringBefore("?")
        ?.substringBefore("/")
        ?.substringAfterLast(".")
        ?.let { route ->
            screen = when (route) {
                Route.Home::class.java.simpleName -> Route.Home
                Route.LeaderBoard::class.java.simpleName -> Route.LeaderBoard
                Route.Notification::class.java.simpleName -> Route.Notification
                Route.Profile::class.java.simpleName -> Route.Profile
                Route.EditProfile::class.java.simpleName -> Route.EditProfile
                Route.ChangePassword::class.java.simpleName -> Route.ChangePassword
                Route.Post::class.java.simpleName -> Route.Post()
                else -> Route.About
            }
        }
    return screen
}

/**
 * Function to convert an likes to a string of like or likes.
 * Ex: 1 like, 2 likes.
 */
fun Int.toLikes(): String {
    return if (this == 1) "$this like" else "$this likes"
}

/**
 * Function to get the link type of a link matching regex.
 * Ex: Github, Linkedin, Other links.
 */
fun String.getLinkDetail(): LinkType {
    return when {
        this.matches(Regex("(?:https?://)?(?:www\\.)?github\\.com/[A-Za-z0-9_-]+/?")) -> LinkType.GITHUB
        this.matches(Regex("(?:https?://)?(?:www\\.)?linkedin\\.com/in/[A-Za-z0-9_-]+/?")) -> LinkType.LINKEDIN
        else -> LinkType.MORE
    }
}

/**
 * Function to convert a [User] to a Map of User.
 */
fun User.convertToMap(): HashMap<String, Any> {
    return hashMapOf(
        "username" to username,
        "name" to name,
        "bio" to bio,
        "profileImage" to profileImage,
        "email" to email,
        "currentProject" to currentProject,
        "gitHubLink" to gitHubLink,
        "linkedInLink" to linkedInLink,
        "portfolioLink" to portfolioLink,
        "xp" to xp,
        "attendance" to attendance,
        "semester" to semester,
        "isVerified" to isVerified,
        "userType" to userType,
        "mentor" to mentor
    )
}

/**
 * Function to convert a [Post] to a Map of Post.
 */
fun Post.convertToMap(): HashMap<String, Any> {
    return hashMapOf(
        "userId" to userId,
        "currentProject" to currentProject,
        "projectId" to projectId,
        "teamMembers" to teamMembers,
        "projectProgress" to projectProgress,
        "deadline" to deadline,
        "timeStamp" to timeStamp
    )
}

/**
 * Function to get the current version of the app.
 */
fun Context.getCurrentVersion(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(
            packageName,
            PackageManager.PackageInfoFlags.of(0)
        ).versionName
    } else {
        packageManager.getPackageInfo(packageName, 0).versionName
    }
}

/**
 * Function to retrieve a postId from a [Post].
 * Format: userId-Current_Project-projectId.
 * Ex: ABC123-Project_K-Project123456.
 */
fun Post.toPostId(): String {
    return "${userId}-${currentProject.replace(" ", "_")}-${projectId}"
}