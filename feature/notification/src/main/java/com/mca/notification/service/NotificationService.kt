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

package com.mca.notification.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mca.notification.service.NotificationHelper.Companion.setToken
import com.mca.ui.R

class NotificationService : FirebaseMessagingService() {

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        setToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationUri =
            Uri.parse("android.resource://com.mca.codemonk/raw/${message.notification?.sound}")
        val notificationChannel = NotificationChannel(
            message.notification?.channelId,
            message.data["channel_name"],
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            importance = NotificationManager.IMPORTANCE_HIGH
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            setSound(
                notificationUri,
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
            enableVibration(true)
            setVibrationPattern(longArrayOf(0, 200, 100, 200))
        }
        notificationManager.createNotificationChannel(notificationChannel)

        val intent = Intent(this, Class.forName("com.mca.codemonk.MainActivity"))
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        if (currentUser != null && message.data["user_id"] != currentUser.uid) { // notifications not shown for current user
            val notification =
                NotificationCompat.Builder(this, message.notification?.channelId ?: "")
                    .setContentTitle(message.notification?.title)
                    .setContentText(message.notification?.body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setChannelId(message.notification?.channelId ?: "")
                    .setSmallIcon(R.drawable.notification)
                    .setColor(Color.White.toArgb())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSound(notificationUri)
                    .setVibrate(longArrayOf(0, 200, 100, 200))
                    .build()

            val id = message.data["id"]?.substringAfter("-")?.toInt()
            notificationManager.notify(id ?: 1, notification)
        }
    }
}