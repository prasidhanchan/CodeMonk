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

package com.mca.repository.impl

import com.google.common.truth.Truth.assertThat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mca.remote.NotificationApi
import com.mca.repository.NotificationRepository
import com.mca.util.constant.Constant.EVENT_CHANNEL_ID
import com.mca.util.constant.Constant.EVENT_TOPIC
import com.mca.util.model.Android
import com.mca.util.model.AndroidNotification
import com.mca.util.model.Data
import com.mca.util.model.MessageToTopic
import com.mca.util.model.Notification
import com.mca.util.model.PushNotificationTopic
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class NotificationRepositoryTest {

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var userRef: CollectionReference
    private lateinit var notificationRef: DatabaseReference
    private lateinit var firebaseFireStore: FirebaseFirestore
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var notificationApi: NotificationApi

    @Before
    fun setUp() {
        firebaseFireStore = mock(FirebaseFirestore::class.java)
        firebaseDatabase = mock(FirebaseDatabase::class.java)
        userRef = mock(CollectionReference::class.java)
        notificationRef = mock(DatabaseReference::class.java)
        notificationApi = mock(NotificationApi::class.java)
        Mockito.`when`(firebaseFireStore.collection("users")).thenReturn(userRef)
        Mockito.`when`(firebaseDatabase.getReference("notifications")).thenReturn(notificationRef)
        notificationRepository = NotificationRepositoryImpl(
            userRef = userRef,
            notificationRef = notificationRef,
            notificationApi = notificationApi
        )
    }

    @Test
    fun `send notification without title throws error`() = runBlocking {
        notificationRepository.sendNotification(
            pushNotificationTopic = PushNotificationTopic(
                message = MessageToTopic(
                    topic = EVENT_TOPIC,
                    notification = Notification(title = "", body = "This is a test body"),
                    data = Data(
                        id = "123",
                        channel_name = EVENT_TOPIC,
                        user_id = "1",
                        time_stamp = ""
                    ),
                    android = Android(
                        notification = AndroidNotification(channel_id = EVENT_CHANNEL_ID)
                    )
                )
            ),
            pushNotificationToken = null,
            accessToken = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Title cannot be empty.")
            }
        )
    }

    @Test
    fun `send notification without body throws error`() = runBlocking {
        notificationRepository.sendNotification(
            pushNotificationTopic = PushNotificationTopic(
                message = MessageToTopic(
                    topic = EVENT_TOPIC,
                    notification = Notification(title = "This is a test title", body = ""),
                    data = Data(
                        id = "123",
                        channel_name = EVENT_TOPIC,
                        user_id = "1",
                        time_stamp = ""
                    ),
                    android = Android(
                        notification = AndroidNotification(channel_id = EVENT_CHANNEL_ID)
                    )
                )
            ),
            pushNotificationToken = null,
            accessToken = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Message cannot be empty.")
            }
        )
    }
}