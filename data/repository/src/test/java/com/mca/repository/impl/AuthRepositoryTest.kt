package com.mca.repository.impl

import com.google.common.truth.Truth.assertThat
import com.mca.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class AuthRepositoryTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl()
    }

    @Test
    fun `login without email throws error`() = runBlocking {
        authRepository.login(
            email = "",
            password = "test123",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Please enter your email.")
            }
        )
    }

    @Test
    fun `login without password throws error`() = runBlocking {
        authRepository.login(
            email = "test@mail.com",
            password = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Please enter your password.")
            }
        )
    }

    @Test
    fun `forgotPassword without email throws error`() = runBlocking {
        authRepository.forgotPassword(
            email = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Please enter your email.")
            }
        )
    }
}