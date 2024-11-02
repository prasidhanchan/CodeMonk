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
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.messaging.FirebaseMessaging
import com.mca.repository.AuthRepository
import com.mca.util.constant.Constant.EMAIL_REGEX
import com.mca.util.constant.convertToMap
import com.mca.util.constant.trimAll
import com.mca.util.model.User
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val userRef: CollectionReference
) : AuthRepository {

    override suspend fun signUp(
        name: String,
        username: String,
        email: String,
        password: String,
        rePassword: String,
        onSuccess: (Response) -> Unit,
        onError: (Response) -> Unit
    ) {
        try {
            when {
                name.isBlank() -> throw Exception("Please enter your name.")
                username.isBlank() -> throw Exception("Please enter your username.")
                email.isBlank() -> throw Exception("Please enter your email.")
                !email.matches(EMAIL_REGEX) -> throw Exception("Enter a valid email.")
                password.isBlank() -> throw Exception("Please enter your password.")
                rePassword.isBlank() -> throw Exception("Please re-enter your password.")
                password != rePassword -> throw Exception("Passwords do not match.")

                else -> {
                    val querySnap = userRef.get().await()
                    querySnap.forEach { docSnap ->
                        if (docSnap.data["username"] == username) throw Exception("Username already exists.")
                    }

                    val token = FirebaseMessaging.getInstance().token.await()
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            val currentUserId =
                                FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
                            userRef.document(currentUserId).set(
                                User(
                                    userId = currentUserId,
                                    name = name,
                                    username = username,
                                    email = email,
                                    token = token,
                                    userType = "student"
                                )
                                    .trimAll()
                                    .convertToMap()
                            )
                                .addOnSuccessListener {
                                    onSuccess(
                                        Response(responseType = ResponseType.SUCCESS)
                                    )
                                }
                                .addOnFailureListener { error ->
                                    error.localizedMessage?.let { Response(message = it) }
                                }
                        }
                        .addOnFailureListener { error ->
                            if (error is FirebaseAuthException) {
                                when (error.errorCode) {
                                    "ERROR_INVALID_EMAIL" -> onError(Response(message = "Enter a valid email."))

                                    "ERROR_INVALID_CREDENTIAL" -> onError(Response(message = "Invalid credentials."))

                                    "ERROR_TOO_MANY_REQUESTS" -> onError(Response(message = "Too many attempts, try after some time."))

                                    "ERROR_NETWORK_REQUEST_FAILED" -> onError(Response(message = "Please check your network connection."))

                                    else -> onError(Response(message = "An unknown error has occurred!"))
                                }
                            }
                        }
                        .await()
                }
            }
        } catch (e: Exception) {
            if (e is FirebaseAuthException) {
                when (e.errorCode) {
                    "ERROR_INVALID_EMAIL" -> onError(Response(message = "Enter a valid email."))

                    "ERROR_INVALID_CREDENTIAL" -> onError(Response(message = "Invalid credentials."))

                    "ERROR_TOO_MANY_REQUESTS" -> onError(Response(message = "Too many attempts, try after some time."))

                    "ERROR_NETWORK_REQUEST_FAILED" -> onError(Response(message = "Please check your network connection."))

                    else -> onError(Response(message = "An unknown error has occurred!"))
                }
            } else {
                e.localizedMessage?.let { error ->
                    onError(Response(message = error))
                }
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String,
        onSuccess: (Response) -> Unit,
        onError: (Response) -> Unit
    ) {
        try {
            if (email.isEmpty()) throw Exception("Please enter your email.")
            if (password.isEmpty()) throw Exception("Please enter your password.")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    onSuccess(
                        Response(responseType = ResponseType.SUCCESS)
                    )
                }
                .addOnFailureListener { error ->
                    if (error is FirebaseAuthException) {
                        when (error.errorCode) {
                            "ERROR_INVALID_EMAIL" -> onError(Response(message = "Enter a valid email."))

                            "ERROR_INVALID_CREDENTIAL" -> onError(Response(message = "Invalid credentials."))

                            "ERROR_USER_DISABLED" -> onError(Response(message = "User disabled. Contact support."))

                            "ERROR_TOO_MANY_REQUESTS" -> onError(Response(message = "Too many attempts, try after some time."))

                            "ERROR_NETWORK_REQUEST_FAILED" -> onError(Response(message = "Please check your network connection."))

                            else -> onError(Response(message = "An unknown error has occurred!"))
                        }
                    }
                }
                .await()
        } catch (e: Exception) {
            if (e is FirebaseAuthException) {
                when (e.errorCode) {
                    "ERROR_INVALID_EMAIL" -> onError(Response(message = "Enter a valid email."))

                    "ERROR_INVALID_CREDENTIAL" -> onError(Response(message = "Invalid credentials."))

                    "ERROR_USER_DISABLED" -> onError(Response(message = "User disabled. Contact support."))

                    "ERROR_TOO_MANY_REQUESTS" -> onError(Response(message = "Too many attempts, try after some time."))

                    "ERROR_NETWORK_REQUEST_FAILED" -> onError(Response(message = "Please check your network connection."))

                    else -> onError(Response(message = "An unknown error has occurred!"))
                }
            } else {
                e.localizedMessage?.let { error ->
                    onError(Response(message = error))
                }
            }
        }
    }

    override suspend fun forgotPassword(
        email: String,
        onSuccess: (Response) -> Unit,
        onError: (Response) -> Unit
    ) {
        try {
            if (email.isEmpty()) throw Exception("Please enter your email.")
            if (email.isNotBlank() && !email.matches(EMAIL_REGEX)) throw Exception("Enter a valid email.")

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    onSuccess(
                        Response(
                            message = "Password reset link sent to your email.",
                            responseType = ResponseType.SUCCESS
                        )
                    )
                }
                .addOnFailureListener { error ->
                    if (error is FirebaseAuthException) {
                        when (error.errorCode) {
                            "ERROR_INVALID_EMAIL" -> onError(Response(message = "Enter a valid email."))

                            "ERROR_TOO_MANY_REQUESTS" -> onError(Response(message = "Too many attempts, try after some time."))

                            "ERROR_NETWORK_REQUEST_FAILED" -> onError(Response(message = "Please check your network connection."))

                            else -> onError(Response(message = "An unknown error has occurred!"))
                        }
                    }
                }
                .await()
        } catch (e: Exception) {
            if (e is FirebaseAuthException) {
                when (e.errorCode) {
                    "ERROR_INVALID_EMAIL" -> onError(Response(message = "Enter a valid email."))

                    "ERROR_TOO_MANY_REQUESTS" -> onError(Response(message = "Too many attempts, try after some time."))

                    "ERROR_NETWORK_REQUEST_FAILED" -> onError(Response(message = "Please check your network connection."))

                    else -> onError(Response(message = "An unknown error has occurred!"))
                }
            } else {
                e.localizedMessage?.let { error ->
                    onError(Response(message = error))
                }
            }
        }
    }
}