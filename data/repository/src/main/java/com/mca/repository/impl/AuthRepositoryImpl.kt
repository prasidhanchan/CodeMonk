package com.mca.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.mca.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {

    override suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (email.isEmpty()) throw Exception("Please enter your email.")
            if (password.isEmpty()) throw Exception("Please enter your password.")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { error ->
                    if (error is FirebaseAuthException) {
                        when(error.errorCode) {
                            "ERROR_INVALID_EMAIL" -> onError("Enter a valid email.")
                            "ERROR_INVALID_CREDENTIAL" -> onError("Invalid credentials.")
                            "ERROR_TOO_MANY_REQUESTS" -> onError("Too many attempts, try after some time.")
                            "ERROR_NETWORK_REQUEST_FAILED" -> onError("Please check your network connection.")
                            else -> onError("An unknown error has occurred!")
                        }
                    }
                }
        } catch (e: Exception) {
            if (e is FirebaseAuthException) {
                when(e.errorCode) {
                    "ERROR_INVALID_EMAIL" -> onError("Enter a valid email.")
                    "ERROR_INVALID_CREDENTIAL" -> onError("Invalid credentials.")
                    "ERROR_TOO_MANY_REQUESTS" -> onError("Too many attempts, try after some time.")
                    "ERROR_NETWORK_REQUEST_FAILED" -> onError("Please check your network connection.")
                    else -> onError("An unknown error has occurred!")
                }
            } else {
                e.localizedMessage?.let(onError)
            }
        }
    }
}