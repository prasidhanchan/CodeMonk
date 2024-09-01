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

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import com.mca.repository.ProfileRepository
import com.mca.util.constants.convertToMap
import com.mca.util.model.User
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    val userRef: CollectionReference
) : ProfileRepository {

    override suspend fun getUser(currentUserId: String): DataOrException<User, Boolean, Exception> {
        val dataOrException: DataOrException<User, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            userRef.document(currentUserId).get()
                .addOnSuccessListener { docSnap ->
                    dataOrException.data = docSnap.toObject<User>()
                }
                .addOnFailureListener { error ->
                    dataOrException.exception = error
                }
                .await()
        } catch (e: Exception) {
            dataOrException.exception = e
        }
        dataOrException.loading = false
        return dataOrException
    }

    override suspend fun updateUser(
        user: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val querySnap = userRef.get().await()

            querySnap.forEach { docSnap ->
                if (docSnap.data["userId"] != user.userId && docSnap.data["username"] == user.username) {
                    throw Exception("Username already exists.")
                }
            }

            when {
                user.username.isEmpty() -> throw Exception("Username cannot be empty.")
                !user.username.matches(Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$\n")) -> throw Exception("Invalid username.")
                user.name.isEmpty() -> throw Exception("Name cannot be empty.")
                user.bio.isEmpty() -> throw Exception("Please add a bio.")
                else -> {
                    userRef.document(user.userId).update(user.convertToMap())
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

    override suspend fun removeProfilePic(
        currentUserId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            userRef.document(currentUserId).update(mapOf("profileImage" to ""))
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

    override suspend fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}