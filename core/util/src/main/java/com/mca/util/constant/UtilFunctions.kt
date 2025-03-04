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

package com.mca.util.constant

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.navigation.NavBackStackEntry
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.mca.util.constant.Constant.COMPRESSION_LEVEL
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.model.NotificationData
import com.mca.util.model.Post
import com.mca.util.model.PushNotificationTopic
import com.mca.util.model.User
import com.mca.util.navigation.Route
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class LinkType {
    GITHUB,
    LINKEDIN,
    MORE
}

enum class PostType {
    PROJECT,
    ANNOUNCEMENT
}

/**
 * Data class to hold an image and its mime type.
 */
data class ImageData(
    val image: ByteArray?,
    val mimeType: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageData

        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        return image?.contentHashCode() ?: 0
    }
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
 * Function to get the notification time stamp.
 * Ex: Just now, 1 minute ago, 2 hours ago, etc.
 */
fun Long.toNotificationTimeStamp(): String {
    val currentTime = System.currentTimeMillis()
    val diff = currentTime - this

    return when {
        diff <= 60_000 -> "Just now"
        diff <= 3_600_000 -> {
            val minutes = diff / 60_000
            if (minutes == 1L) "$minutes minute ago" else "$minutes minutes ago"
        }

        diff <= 86_400_000 -> {
            val hours = diff / 3_600_000
            if (hours == 1L) "$$hours hour ago" else "$hours hours ago"
        }

        diff <= 2_592_000_000 -> {
            val days = diff / 86_400_000
            if (days == 1L) "$days day ago" else "$days days ago"
        }

        diff <= 31_104_000_000 -> {
            val weeks = diff / 2_592_000_000
            if (weeks == 1L) "$weeks week ago" else "$weeks weeks ago"
        }

        diff <= 31_104_000_000_000 -> {
            val months = diff / 31_104_000_000
            if (months == 1L) "$months month ago" else "$months months ago"
        }

        else -> {
            val years = diff / 31_104_000_000_000
            if (years == 1L) "$years year ago" else "$years years ago"
        }
    }
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
                Route.Search::class.java.simpleName -> Route.Search
                Route.SendNotification::class.java.simpleName -> Route.SendNotification
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
        this.matches(Regex("(?:https?://)?(?:www\\.)?(?:linkedin\\.com/in|linked\\.in)/[A-Za-z0-9_-]+/?")) -> LinkType.LINKEDIN
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
        "userId" to userId,
        "token" to token,
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
        "verified" to verified,
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
        "postType" to postType,
        "currentProject" to currentProject,
        "postId" to postId,
        "images" to images,
        "teamMembers" to teamMembers,
        "description" to description,
        "projectProgress" to projectProgress,
        "deadline" to deadline,
        "timeStamp" to timeStamp
    )
}

/**
 * Function to get the current version of the app.
 */
fun Context.getCurrentVersion(): String? {
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
 * Format: userId-postId.
 * Ex: ABC123-Post123456.
 */
fun Post.toPostId(): String {
    return "${userId}-${postId}"
}

/**
 * Function to check if a [User] matches a search query.
 */
fun User.matchUsernameAndName(search: String): Boolean {
    return when {
        username.length >= 3 &&
                username.contains(search, ignoreCase = true) &&
                username.startsWith(search, ignoreCase = true) -> true

        username.length >= 3 &&
                name.contains(search, ignoreCase = true) &&
                name.startsWith(search, ignoreCase = true) -> true

        username.length >= 3 &&
                name.contains(search, ignoreCase = true) &&
                name.substringAfter(" ").startsWith(search, ignoreCase = true) -> true

        username.length >= 3 &&
                name.contains(search, ignoreCase = true) &&
                name.substringAfterLast(" ").startsWith(search, ignoreCase = true) -> true

        else -> false
    }
}

/**
 * Function to check if a username of a [User] matches a search query.
 */
fun String.matchUsername(username: String): Boolean {
    return when {
        username.length >= 3 &&
                startsWith(username, ignoreCase = true) &&
                contains(username, ignoreCase = true) -> true

        username.length >= 3 &&
                substringAfter("_").startsWith(username, ignoreCase = true) &&
                substringAfter("_").contains(username, ignoreCase = true) -> true

        username.length >= 3 &&
                substringAfterLast("_").startsWith(username, ignoreCase = true) &&
                substringAfter("_").contains(username, ignoreCase = true) -> true

        else -> false
    }
}

/**
 * Function to convert a [PushNotificationTopic] to a [NotificationData].
 */
fun PushNotificationTopic.toNotification(): HashMap<String, Any> {
    return hashMapOf(
        "id" to message.data.id,
        "title" to message.notification.title,
        "body" to message.notification.body,
        "timeStamp" to message.data.time_stamp.toLong()
    )
}

/**
 * Function to load native ads
 * @param context Requires a [Context].
 * @param adUnitId requires a unique Ad unit ID.
 * @param isLoading Indicator if the ad is being loaded.
 * @param onAdLoaded The lambda triggered when the ads are loaded.
 * @param maxAds The max number of ads that are to be loaded. Can only load max 5 ads
 * @param adChoicesPlacement The placement of the ad choices icon.
 */
fun loadNativeAds(
    context: Context,
    adUnitId: String,
    isLoading: (Boolean) -> Unit = { },
    onAdLoaded: (NativeAd?) -> Unit,
    maxAds: Int = 5,
    adChoicesPlacement: Int = 2
) {
    lateinit var adLoader: AdLoader

    val builder = AdLoader.Builder(context, adUnitId)
        .forNativeAd { nativeAd ->
            onAdLoaded(nativeAd)
            isLoading(adLoader.isLoading)
        }

    adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
            onAdLoaded(null)
        }
    })
        .withNativeAdOptions(
            NativeAdOptions.Builder()
                .setAdChoicesPlacement(adChoicesPlacement)
                .build()
        )
        .build()

    adLoader.loadAds(AdRequest.Builder().build(), maxAds)
}

/**
 * Function to check if the string is a local or firebase uri and is not blank.
 */
fun String.isLocalUriAndNotBlank(): Boolean =
    !contains("https://firebasestorage.googleapis.com/") && isNotBlank()

/**
 * Function to trim all the whitespaces from [User].
 */
fun User.trimAll(): User {
    return copy(
        username = username.trim(),
        name = name.trim(),
        bio = bio.trim(),
        currentProject = currentProject.trim(),
        gitHubLink = gitHubLink.trim(),
        linkedInLink = linkedInLink.trim(),
        portfolioLink = portfolioLink.trim(),
        mentor = mentor.trim(),
    )
}

/**
 * Function to trim all the whitespaces from [Post].
 */
fun Post.trimAll(): Post {
    return copy(
        currentProject = currentProject.trim(),
        description = description.trim(),
        deadline = deadline.trim(),
        teamMembers = teamMembers.map { it.trim() }
    )
}

/**
 * Function to check app updates.
 * @param appUpdateManager Requires [AppUpdateManager].
 * @param updateType The type of update to check for.
 * @param activity Requires an [Activity].
 */
fun checkUpdates(
    appUpdateManager: AppUpdateManager,
    updateType: Int = AppUpdateType.FLEXIBLE,
    activity: Activity
) {
    appUpdateManager.appUpdateInfo
        .addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = info.isUpdateTypeAllowed(updateType)

            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlow(
                    info,
                    activity,
                    AppUpdateOptions.defaultOptions(updateType)
                )
            }
        }
}

/**
 * Function to compress and scale an image to lower quality.
 * Scales an image if its greater that 1412x949 pixels.
 * @param context Requires a [Context].
 * @param uri Requires a local image uri.
 * @return Return a [ImageData] of the compressed image.
 */
fun compressImage(context: Context, uri: Uri): ImageData? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val width = if (bitmap.width > 1412f) 1412 else bitmap.width
        val height = if (bitmap.height > 949f) {
            (width.toFloat() / aspectRatio).toInt()
        } else {
            bitmap.height
        }
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

        val byteArrayOutputStream = ByteArrayOutputStream()
        val mimeType = context.contentResolver.getType(uri)
        val format = when (mimeType) {
            "image/jpeg" -> Bitmap.CompressFormat.JPEG
            "image/png" -> Bitmap.CompressFormat.PNG
            else -> Bitmap.CompressFormat.JPEG
        }
        resizedBitmap.compress(
            format,
            COMPRESSION_LEVEL,
            byteArrayOutputStream
        )
        // Return image with meta data
        ImageData(
            image = byteArrayOutputStream.toByteArray(),
            mimeType = when (mimeType) {
                "image/jpeg" -> "jpeg"
                "image/png" -> "png"
                else -> "jpg"
            }
        )
    } catch (e: Exception) {
        showSnackBar(
            response = Response(
                message = e.localizedMessage,
                responseType = ResponseType.ERROR
            )
        )
        null
    }
}

/**
 * Function to extract a url from a string.
 */
fun String.extractUrl(): String? {
    return Regex("""https?://(www\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)""")
        .find(this)?.value
}