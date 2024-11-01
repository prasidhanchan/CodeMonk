package com.mca.repository.impl

import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mca.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock

class AuthRepositoryTest {

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var collectionReference: CollectionReference
    private lateinit var fireStore: FirebaseFirestore

    @Before
    fun setUp() {
        fireStore = mock(FirebaseFirestore::class.java)
        collectionReference = mock(CollectionReference::class.java)
        Mockito.`when`(fireStore.collection("testing")).thenReturn(collectionReference)
        authRepository = AuthRepositoryImpl(userRef = collectionReference)
    }

    @Test
    fun `signUp without name throws error`() = runBlocking {
        authRepository.signUp(
            name = "",
            username = "test",
            email = "test@mail.com",
            password = "test123",
            rePassword = "test123",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Please enter your name.")
            }
        )
    }

    @Test
    fun `signUp without username throws error`() = runBlocking {
        authRepository.signUp(
            name = "Test",
            username = "",
            email = "test@mail.com",
            password = "test123",
            rePassword = "test123",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Please enter your username.")
            }
        )
    }

    @Test
    fun `signUp without email throws error`() = runBlocking {
        authRepository.signUp(
            name = "Test",
            username = "test_123",
            email = "",
            password = "test123",
            rePassword = "test123",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Please enter your email.")
            }
        )
    }

    @Test
    fun `signUp with invalid email throws error`() = runBlocking {
        authRepository.signUp(
            name = "Test",
            username = "test_123",
            email = "test@gmail.com",
            password = "test123",
            rePassword = "test123",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Enter a valid email.")
            }
        )
    }

    @Test
    fun `signUp without password throws error`() = runBlocking {
        authRepository.signUp(
            name = "Test",
            username = "test_123",
            email = "nnm23mc101@nmamit.in",
            password = "",
            rePassword = "test123",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Please enter your password.")
            }
        )
    }

    @Test
    fun `signUp without re-password throws error`() = runBlocking {
        authRepository.signUp(
            name = "Test",
            username = "test_123",
            email = "nnm23mc101@nmamit.in",
            password = "test123",
            rePassword = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Please re-enter your password.")
            }
        )
    }

    @Test
    fun `signUp with different passwords throws error`() = runBlocking {
        authRepository.signUp(
            name = "Test",
            username = "test_123",
            email = "nnm23mc101@nmamit.in",
            password = "test123",
            rePassword = "test1234",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Passwords do not match.")
            }
        )
    }

    @Test
    fun `login without email throws error`() = runBlocking {
        authRepository.login(
            email = "",
            password = "test123",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Please enter your email.")
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
                assertThat(error.message).isEqualTo("Please enter your password.")
            }
        )
    }

    @Test
    fun `forgotPassword without email throws error`() = runBlocking {
        authRepository.forgotPassword(
            email = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error.message).isEqualTo("Please enter your email.")
            }
        )
    }
}