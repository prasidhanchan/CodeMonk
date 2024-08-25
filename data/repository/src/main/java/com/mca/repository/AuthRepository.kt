package com.mca.repository

import com.mca.util.warpper.Response

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String,
        onSuccess: (Response) -> Unit,
        onError: (Response) -> Unit
    )

    suspend fun forgotPassword(
        email: String,
        onSuccess: (Response) -> Unit,
        onError: (Response) -> Unit
    )
}