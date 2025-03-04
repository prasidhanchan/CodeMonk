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

package com.mca.repository.impl

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import com.mca.repository.BuildConfig.UPDATE_CHANNEL
import com.mca.repository.ProfileRepository
import com.mca.util.constant.Constant.ADMIN
import com.mca.util.constant.Constant.USERNAME_REGEX
import com.mca.util.constant.convertToMap
import com.mca.util.constant.isLocalUriAndNotBlank
import com.mca.util.constant.matchUsername
import com.mca.util.constant.trimAll
import com.mca.util.model.Tag
import com.mca.util.model.TopMember
import com.mca.util.model.Update
import com.mca.util.model.User
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

class ProfileRepositoryImpl @Inject constructor(
    val userRef: CollectionReference,
    val updateRef: CollectionReference,
    val leaderBoardRef: CollectionReference,
    val userStorage: StorageReference
) : ProfileRepository {

    override suspend fun getUser(currentUserId: String): DataOrException<User, Boolean, Exception> {
        val dataOrException: DataOrException<User, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            val token = FirebaseMessaging.getInstance().token.await() // Get FCM token
            userRef.document(currentUserId).get()
                .addOnSuccessListener { docSnap ->
                    dataOrException.data = docSnap.toObject<User>()
                    if (dataOrException.data == null) {
                        // Create user details if empty
                        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
                        userRef.document(currentUserId)
                            .set(
                                User(
                                    username = "cm_user_${Random.nextInt(0, Int.MAX_VALUE)}",
                                    name = if (currentUserEmail?.contains("tester") == true) "Tester" else "Code Monk User",
                                    userId = currentUserId,
                                    userType = if (currentUserEmail?.contains("tester") == true) "tester" else "student",
                                    email = currentUserEmail.orEmpty(),
                                    token = token.orEmpty()
                                )
                                    .trimAll()
                                    .convertToMap()
                            )
                    }
                }
                .addOnFailureListener { error ->
                    dataOrException.exception = error
                }
                .await()
        } catch (e: Exception) {
            dataOrException.exception = e
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

    override suspend fun updateUser(
        user: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            when {
                user.username.isEmpty() -> throw Exception("Username cannot be empty.")
                !user.username.matches(USERNAME_REGEX) -> throw Exception("Invalid username.")
                user.name.isEmpty() -> throw Exception("Name cannot be empty.")
                user.bio.isEmpty() -> throw Exception("Please add a bio.")
                user.mentor.isEmpty() && user.userType != ADMIN -> throw Exception("Please add a mentor.")

                else -> {
                    val querySnap = userRef.get().await()
                    querySnap.forEach { docSnap ->
                        if (docSnap.data["userId"] != user.userId && docSnap.data["username"] == user.username) {
                            throw Exception("Username already exists.")
                        }
                    }

                    userRef.document(user.userId)
                        .update(user.trimAll().convertToMap())
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { error ->
                            error.localizedMessage?.let(onError)
                        }
                        .await()
                }
            }
        } catch (e: Exception) {
            e.localizedMessage?.let(onError)
        }
    }

    override suspend fun updateImageUrl(
        user: User,
        onSuccess: (url: String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (user.profileImage.isLocalUriAndNotBlank()) {
                val taskSnap = userStorage.child("${user.userId}.jpg")
                    .putFile(user.profileImage.toUri())
                    .addOnFailureListener { error ->
                        error.localizedMessage?.let(onError)
                    }
                    .await()

                taskSnap.storage.downloadUrl
                    .addOnSuccessListener { uri ->
                        onSuccess(uri.toString())
                    }
                    .addOnFailureListener { error ->
                        error.localizedMessage?.let(onError)
                    }
                    .await()
            } else {
                onSuccess("")
            }

        } catch (e: Exception) {
            e.localizedMessage?.let(onError)
        }
    }

    override suspend fun removeProfilePic(
        user: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            userRef.document(user.userId).update(mapOf("profileImage" to ""))
                .addOnSuccessListener {
                    userStorage.child("${user.userId}.jpg").delete()
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { error ->
                            error.localizedMessage?.let(onError)
                        }
                }
                .addOnFailureListener { error ->
                    error.localizedMessage?.let(onError)
                }
                .await()
        } catch (e: Exception) {
            e.localizedMessage?.let(onError)
        }
    }

    override suspend fun logout() = FirebaseAuth.getInstance().signOut()

    override suspend fun changePassword(
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (password.isBlank()) throw Exception("Password cannot be empty.")

            FirebaseAuth.getInstance().currentUser?.updatePassword(password)
                ?.addOnSuccessListener {
                    onSuccess()
                }
                ?.addOnFailureListener { error ->
                    if (error is FirebaseAuthException) {
                        when (error.errorCode) {
                            "ERROR_REQUIRES_RECENT_LOGIN" -> onError("This operation requires a recent login.")
                            "ERROR_WEAK_PASSWORD" -> onError("Password should be at least 6 characters.")
                            else -> onError("An unknown error occurred.")
                        }
                    }
                }
                ?.await()
        } catch (e: Exception) {
            if (e is FirebaseAuthException) {
                when (e.errorCode) {
                    "ERROR_REQUIRES_RECENT_LOGIN" -> onError("This operation requires a recent login.")
                    "ERROR_WEAK_PASSWORD" -> onError("Password should be at least 6 characters.")
                    else -> onError("An unknown error occurred.")
                }
            } else {
                e.localizedMessage?.let(onError)
            }
        }
    }

    override suspend fun getUpdate(): DataOrException<Update, Boolean, Exception> {
        val dataOrException: DataOrException<Update, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            updateRef.document(UPDATE_CHANNEL).get()
                .addOnSuccessListener { docSnap ->
                    dataOrException.data = docSnap.toObject<Update>()
                }
                .addOnFailureListener { error ->
                    dataOrException.exception = error
                }
                .await()
        } catch (e: Exception) {
            dataOrException.exception = e
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

    override suspend fun getSelectedUser(
        userId: String,
        onSuccess: (user: User?) -> Unit,
        onError: (error: String) -> Unit
    ) {
        try {
            userRef.document(userId).get()
                .addOnSuccessListener { docSnap ->
                    try {
                        onSuccess(docSnap.toObject<User>())
                    } catch (_: Exception) {
                        onError("User profile not created!")
                    }
                }
                .addOnFailureListener { error ->
                    error.localizedMessage?.let(onError)
                }
                .await()
        } catch (e: Exception) {
            e.localizedMessage?.let(onError)
        }
    }

    override suspend fun getRandomMentors(selectedUserId: String): DataOrException<List<User>, Boolean, Exception> {
        val dataOrException: DataOrException<List<User>, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            userRef
                .whereEqualTo("userType", ADMIN).get()
                .addOnSuccessListener { querySnap ->
                    dataOrException.data = querySnap.documents.mapNotNull { docSnap ->
                        docSnap.toObject<User>()
                    }
                        .filterNot { user -> user.userId == selectedUserId }
                        .shuffled()
                        .take(3)
                }
                .addOnFailureListener { error ->
                    dataOrException.exception = error
                }
                .await()
                .asFlow()
        } catch (e: Exception) {
            dataOrException.exception = e
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

    override suspend fun getMentorTags(username: String): DataOrException<List<Tag>, Boolean, Exception> {
        val dataOrException: DataOrException<List<Tag>, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            userRef.get()
                .addOnSuccessListener { querySnap ->
                    dataOrException.data = querySnap.documents.mapNotNull { docSnap ->
                        docSnap.toObject<Tag>()
                    }
                        .filter { tag -> tag.username.matchUsername(username) && tag.userType == ADMIN }
                    if (username.isBlank()) dataOrException.data = emptyList()
                }
                .addOnFailureListener { error ->
                    dataOrException.exception = error
                }
                .await()
                .asFlow()
        } catch (e: Exception) {
            dataOrException.exception = e
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

    override suspend fun getTopMembers(): DataOrException<TopMember, Boolean, Exception> {
        val dataOrException: DataOrException<TopMember, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            leaderBoardRef.document("topMembers").get()
                .addOnSuccessListener { docSnap ->
                    dataOrException.data = docSnap.toObject<TopMember>()
                }
                .addOnFailureListener { error ->
                    dataOrException.exception = error
                }
                .await()
        } catch (e: Exception) {
            dataOrException.exception = e
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

    override suspend fun updatePoints(
        newPoints: Int,
        userId: String,
        onSuccess: () -> Unit
    ) {
        var points: Int = newPoints
        userRef.document(userId).get()
            .addOnSuccessListener { docSnap ->
                if (points < 100) points += docSnap.get("xp", Int::class.java)!!
                userRef.document(userId)
                    .update(mapOf("xp" to points))
                    .addOnSuccessListener {
                        onSuccess()
                    }
            }
            .await()
    }
}