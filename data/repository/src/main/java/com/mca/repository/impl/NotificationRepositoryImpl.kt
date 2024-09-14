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

package com.mca.repository.impl

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.CollectionReference
import com.mca.remote.NotificationApi
import com.mca.repository.NotificationRepository
import com.mca.util.R
import com.mca.util.constant.Constant.SCOPE
import com.mca.util.constant.toNotification
import com.mca.util.model.NotificationData
import com.mca.util.model.PushNotification
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    val userRef: CollectionReference,
    val notificationRef: DatabaseReference,
    val notificationApi: NotificationApi
) : NotificationRepository {

    override suspend fun upsertToken(
        newToken: String,
        userId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            userRef.document(userId)
                .update("token", newToken)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { error ->
                    error.localizedMessage?.let(onError)
                }
                .await()
        } catch (e: Exception) {
            e.localizedMessage?.let(onError)
        }
    }

    override suspend fun getAccessToken(context: Context): DataOrException<String, Boolean, Exception> {
        val dataOrException: DataOrException<String, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            val stream = context.resources.openRawResource(R.raw.codemonk_service_key)
            val credentials = GoogleCredentials
                .fromStream(stream)
                .createScoped(listOf(SCOPE))

            credentials.refreshIfExpired()
            dataOrException.data = credentials.accessToken.tokenValue
        } catch (e: Exception) {
            dataOrException.exception = e
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

    override suspend fun sendNotification(
        pushNotification: PushNotification,
        accessToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (pushNotification.message.notification.title.isEmpty()) throw Exception("Title cannot be empty.")
            if (pushNotification.message.notification.body.isEmpty()) throw Exception("Message cannot be empty.")

            val response = notificationApi.postNotification(
                headers = hashMapOf(
                    "Authorization" to "Bearer $accessToken",
                    "Content-Type" to "application/json"
                ),
                pushNotification = pushNotification
            )
            if (response.isSuccessful) {
                // Add notification data to firebase database
                notificationRef.child(pushNotification.message.data.id)
                    .setValue(pushNotification.toNotification())
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { error ->
                        error.localizedMessage?.let(onError)
                    }
                    .await()
            } else {
                onError("Something went wrong! ${response.code()}")
            }
        } catch (e: Exception) {
            e.localizedMessage?.let(onError)
        }
    }

    override suspend fun getNotifications(): Flow<DataOrException<List<NotificationData>, Boolean, Exception>> {
        val dataOrException: MutableStateFlow<DataOrException<List<NotificationData>, Boolean, Exception>> =
            MutableStateFlow(DataOrException(loading = true))

        try {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dataOrException.update {
                        it.copy(
                            data = snapshot.children.map { docSnap ->
                                docSnap.getValue<NotificationData>()!!
                            }
                                .sortedByDescending { notification -> notification.timeStamp }
                        )
                    }
                    dataOrException.update { it.copy(loading = false) }
                }

                override fun onCancelled(error: DatabaseError) {
                    dataOrException.update { it.copy(exception = error.toException()) }
                }
            }
            notificationRef.addValueEventListener(valueEventListener)
        } catch (e: Exception) {
            dataOrException.update { it.copy(exception = e) }
        }
        return dataOrException.asStateFlow()
    }
}