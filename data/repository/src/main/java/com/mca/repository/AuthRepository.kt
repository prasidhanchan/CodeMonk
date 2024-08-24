package com.mca.repository

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}