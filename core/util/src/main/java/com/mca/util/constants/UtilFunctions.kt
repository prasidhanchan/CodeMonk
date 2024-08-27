package com.mca.util.constants

import androidx.navigation.NavBackStackEntry
import com.mca.util.navigation.Route
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                else -> Route.Profile
            }
        }
    return screen
}

/**
 * Function to convert an likes to a string of like or likes.
 * Ex: 1 like, 2 likes
 */
fun Int.toLikes(): String {
    return if (this == 1) "$this like" else "$this likes"
}