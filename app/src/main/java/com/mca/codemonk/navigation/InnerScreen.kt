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

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.nativead.NativeAd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.mca.home.navigation.homeNavigation
import com.mca.home.screen.HomeViewModel
import com.mca.leaderboard.navigation.leaderBoardNavigation
import com.mca.notification.navigation.notificationNavigation
import com.mca.notification.navigation.sendNotificationNavigation
import com.mca.notification.screen.NotificationViewModel
import com.mca.post.navigation.announcementNavigation
import com.mca.post.navigation.postNavigation
import com.mca.profile.navigation.aboutNavigation
import com.mca.profile.navigation.changePasswordNavigation
import com.mca.profile.navigation.editProfileNavigation
import com.mca.profile.navigation.profileNavigation
import com.mca.profile.navigation.viewProfileNavigation
import com.mca.profile.screen.ProfileViewModel
import com.mca.search.BuildConfig.NATIVE_AD_ID_SEARCH
import com.mca.search.navigation.searchNavigation
import com.mca.ui.R
import com.mca.ui.component.CMBottomBar
import com.mca.ui.theme.Black
import com.mca.util.constant.Constant.ANNOUNCEMENT_CHANNEL_ID
import com.mca.util.constant.Constant.ANNOUNCEMENT_TOPIC
import com.mca.util.constant.Constant.EVENT_TOPIC
import com.mca.util.constant.Constant.LIKE_CHANNEL_ID
import com.mca.util.constant.Constant.LIKE_TOPIC
import com.mca.util.constant.Constant.MAX_POST_ADS
import com.mca.util.constant.Constant.MAX_SEARCH_ADS
import com.mca.util.constant.Constant.POST_CHANNEL_ID
import com.mca.util.constant.Constant.POST_TOPIC
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.constant.getCurrentRoute
import com.mca.util.constant.loadNativeAds
import com.mca.util.model.Android
import com.mca.util.model.AndroidNotification
import com.mca.util.model.Data
import com.mca.util.model.MessageToToken
import com.mca.util.model.MessageToTopic
import com.mca.util.model.Notification
import com.mca.util.model.PushNotificationToken
import com.mca.util.model.PushNotificationTopic
import com.mca.util.navigation.Route
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun NavGraphBuilder.innerScreen(
    navigateToLogin: () -> Unit
) {
    composable<Route.InnerScreen> {
        val viewModelHome: HomeViewModel = hiltViewModel()
        val viewModelProfile: ProfileViewModel = hiltViewModel()
        val viewModelNotification: NotificationViewModel = hiltViewModel()
        val uiStateProfile by viewModelProfile.uiState.collectAsStateWithLifecycle()
        val uiStateNotify by viewModelNotification.uiState.collectAsStateWithLifecycle()

        val navHostController = rememberNavController()
        val navaBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navaBackStackEntry?.getCurrentRoute()

        val currentUser = FirebaseAuth.getInstance().currentUser

        val showBottomBar = when (currentRoute) {
            Route.Home -> true
            Route.LeaderBoard -> true
            Route.Notification -> true
            Route.Profile -> true
            else -> false
        }

        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { result ->
                if (!result) {
                    showSnackBar(
                        response = Response(
                            message = context.getString(R.string.notification_permission_denied),
                            responseType = ResponseType.ERROR
                        )
                    )
                }
            }
        )

        LaunchedEffect(key1 = Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_DENIED &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Load user data when the app starts
        LaunchedEffect(key1 = uiStateProfile.currentUser.userId) {
            if (uiStateProfile.currentUser.userId.isEmpty()) viewModelProfile.getUser()

            // Subscribe to topics
            FirebaseMessaging.getInstance().apply {
                subscribeToTopic(EVENT_TOPIC)
                subscribeToTopic(POST_TOPIC)
                subscribeToTopic(ANNOUNCEMENT_TOPIC)
                subscribeToTopic(LIKE_TOPIC)
            }

            // get Access token for notification
            viewModelNotification.getAccessToken(context)
        }

        val nativeAds = remember { mutableStateListOf<NativeAd?>() }
        val nativeAdsSearch = remember { mutableStateListOf<NativeAd?>() }
        LaunchedEffect(key1 = Unit) {
            loadNativeAds(
                context = context,
                adUnitId = NATIVE_AD_ID_SEARCH,
                onAdLoaded = { ad ->
                    nativeAds.add(ad)
                },
                maxAds = MAX_POST_ADS
            )
            loadNativeAds(
                context = context,
                adUnitId = NATIVE_AD_ID_SEARCH,
                onAdLoaded = { ad ->
                    nativeAdsSearch.add(ad)
                },
                maxAds = MAX_SEARCH_ADS,
                adChoicesPlacement = 1
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Black,
            bottomBar = {
                CMBottomBar(
                    visible = showBottomBar,
                    isNewNotification = if (uiStateNotify.notifications.isEmpty() || uiStateProfile.currentUser.lastSeen == 0L) false
                    else uiStateNotify.notifications[0].timeStamp > uiStateProfile.currentUser.lastSeen,
                    navHostController = navHostController,
                    currentRoute = currentRoute
                )
            },
            contentColor = Black
        ) {
            NavHost(
                navController = navHostController,
                startDestination = Route.Home,
            ) {
                homeNavigation(
                    viewModel = viewModelHome,
                    navHostController = navHostController,
                    profileImage = uiStateProfile.currentUser.profileImage,
                    currentUserId = currentUser?.uid ?: "",
                    currentUsername = uiStateProfile.currentUser.username,
                    currentUserType = uiStateProfile.currentUser.userType,
                    onDeletedClick = viewModelHome::deletePost,
                    sendLikeNotification = { token ->
                        viewModelNotification.sendNotificationToToken(
                            pushNotification = PushNotificationToken(
                                message = MessageToToken(
                                    token = token,
                                    notification = Notification(
                                        title = context.getString(
                                            R.string.liked,
                                            uiStateProfile.currentUser.name
                                        ),
                                        body = context.getString(
                                            R.string.new_like_body,
                                            uiStateProfile.currentUser.username
                                        )
                                    ),
                                    data = Data(
                                        id = "$LIKE_TOPIC-${Random.nextInt(0, Int.MAX_VALUE)}",
                                        channel_name = LIKE_TOPIC,
                                        time_stamp = System.currentTimeMillis().toString(),
                                        user_id = currentUser?.uid ?: ""
                                    ),
                                    android = Android(
                                        notification = AndroidNotification(channel_id = LIKE_CHANNEL_ID)
                                    )
                                )
                            ),
                            accessToken = uiStateNotify.accessToken ?: ""
                        )
                    },
                    nativeAds = nativeAds
                )
                profileNavigation(
                    viewModel = viewModelProfile,
                    navHostController = navHostController,
                    onLogoutClick = {
                        viewModelProfile.logout()
                        navigateToLogin()
                    }
                )
                editProfileNavigation(
                    viewModel = viewModelProfile,
                    navHostController = navHostController
                )
                changePasswordNavigation(
                    viewModel = viewModelProfile,
                    navHostController = navHostController
                )
                aboutNavigation(
                    viewModel = viewModelProfile,
                    navHostController = navHostController
                )
                postNavigation(
                    userType = uiStateProfile.currentUser.userType,
                    navHostController = navHostController,
                    sendPostNotification = {
                        viewModelNotification.sendNotificationToTopic(
                            pushNotification = PushNotificationTopic(
                                message = MessageToTopic(
                                    topic = POST_TOPIC,
                                    notification = Notification(
                                        title = context.getString(R.string.new_post),
                                        body = context.getString(
                                            R.string.new_post_body,
                                            uiStateProfile.currentUser.username
                                        )
                                    ),
                                    data = Data(
                                        id = "$POST_TOPIC-${Random.nextInt(0, Int.MAX_VALUE)}",
                                        channel_name = POST_TOPIC,
                                        time_stamp = System.currentTimeMillis().toString(),
                                        user_id = currentUser?.uid ?: ""
                                    ),
                                    android = Android(
                                        notification = AndroidNotification(channel_id = POST_CHANNEL_ID)
                                    )
                                )
                            ),
                            accessToken = uiStateNotify.accessToken ?: ""
                        )
                    }
                )
                announcementNavigation(
                    navHostController = navHostController,
                    sendNotification = {
                        viewModelNotification.sendNotificationToTopic(
                            pushNotification = PushNotificationTopic(
                                message = MessageToTopic(
                                    topic = ANNOUNCEMENT_TOPIC,
                                    notification = Notification(
                                        title = context.getString(R.string.new_announcement),
                                        body = context.getString(
                                            R.string.new_announcement_body,
                                            uiStateProfile.currentUser.username
                                        )
                                    ),
                                    data = Data(
                                        id = "$ANNOUNCEMENT_TOPIC-${Random.nextInt(0, Int.MAX_VALUE)}",
                                        channel_name = ANNOUNCEMENT_TOPIC,
                                        time_stamp = System.currentTimeMillis().toString(),
                                        user_id = currentUser?.uid ?: ""
                                    ),
                                    android = Android(
                                        notification = AndroidNotification(channel_id = ANNOUNCEMENT_CHANNEL_ID)
                                    )
                                )
                            ),
                            accessToken = uiStateNotify.accessToken ?: ""
                        )
                    }
                )
                viewProfileNavigation(navHostController = navHostController)
                searchNavigation(
                    navHostController = navHostController,
                    nativeAds = nativeAdsSearch
                )
                leaderBoardNavigation(navHostController)
                notificationNavigation(
                    viewModel = viewModelNotification,
                    userType = uiStateProfile.currentUser.userType,
                    navHostController = navHostController,
                    refreshUser = viewModelProfile::getUser
                )
                sendNotificationNavigation(
                    viewModel = viewModelNotification,
                    navHostController = navHostController
                )
            }
        }
    }
}