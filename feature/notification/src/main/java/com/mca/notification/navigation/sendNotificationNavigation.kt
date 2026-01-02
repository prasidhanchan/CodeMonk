/*
 * Copyright © 2026 Prasidh Gopal Anchan
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

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.mca.notification.screen.NotificationViewModel
import com.mca.notification.screen.SendNotificationScreen
import com.mca.ui.R
import com.mca.util.constant.Constant.EVENT_CHANNEL_ID
import com.mca.util.constant.Constant.EVENT_TOPIC
import com.mca.util.constant.Constant.IN_OUT_DURATION
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.model.Android
import com.mca.util.model.AndroidNotification
import com.mca.util.model.Data
import com.mca.util.model.MessageToTopic
import com.mca.util.model.Notification
import com.mca.util.model.PushNotificationTopic
import com.mca.util.navigation.Route
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import kotlin.random.Random

fun NavGraphBuilder.sendNotificationNavigation(
    viewModel: NotificationViewModel,
    navHostController: NavHostController
) {
    composable<Route.SendNotification>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        }
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val context = LocalContext.current

        val currentUser = FirebaseAuth.getInstance().currentUser

        val pushNotification = PushNotificationTopic(
            message = MessageToTopic(
                topic = EVENT_TOPIC,
                notification = Notification(
                    title = uiState.title,
                    body = uiState.message
                ),
                data = Data(
                    id = "$EVENT_TOPIC-${Random.nextInt(0, Int.MAX_VALUE)}",
                    channel_name = EVENT_TOPIC,
                    time_stamp = System.currentTimeMillis().toString(),
                    user_id = currentUser?.uid ?: ""
                ),
                android = Android(
                    notification = AndroidNotification(channel_id = EVENT_CHANNEL_ID)
                )
            )
        )

        SendNotificationScreen(
            uiState = uiState,
            onSendClick = {
                viewModel.sendNotificationToTopic(
                    pushNotification = pushNotification,
                    accessToken = uiState.accessToken ?: "",
                    onSuccess = {
                        showSnackBar(
                            response = Response(
                                message = context.getString(R.string.notification_sent),
                                responseType = ResponseType.SUCCESS
                            )
                        )
                    }
                )
            },
            onTitleChange = viewModel::setTitle,
            onMessageChange = viewModel::setMessage,
            onBackClick = { navHostController.popBackStack() }
        )
    }
}