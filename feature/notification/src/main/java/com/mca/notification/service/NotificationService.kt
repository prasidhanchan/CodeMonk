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

package com.mca.notification.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mca.notification.service.NotificationHelper.Companion.setToken
import com.mca.ui.R

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        setToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(
            message.notification?.channelId,
            message.data["channel_name"],
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
        notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
        notificationChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

        val notification = Notification.Builder(this, message.notification?.channelId)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setChannelId(message.notification?.channelId)
            .setSmallIcon(R.drawable.notification)
            .setColor(Color.White.toArgb())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(message.data["id"]?.toInt() ?: 1, notification)
    }
}