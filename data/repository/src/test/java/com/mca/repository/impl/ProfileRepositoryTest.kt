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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mca.repository.ProfileRepository
import com.mca.util.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class ProfileRepositoryTest {

    private lateinit var userRef: CollectionReference
    private lateinit var updateRef: CollectionReference
    private lateinit var leaderBoardRef: CollectionReference
    private lateinit var userStorage: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var profileRepository: ProfileRepository

    @Before
    fun setUp() {
        firebaseFirestore = mock(FirebaseFirestore::class.java)
        firebaseDatabase = mock(FirebaseDatabase::class.java)
        firebaseStorage = mock(FirebaseStorage::class.java)
        userRef = mock(CollectionReference::class.java)
        updateRef = mock(CollectionReference::class.java)
        leaderBoardRef = mock(CollectionReference::class.java)
        userStorage = mock(StorageReference::class.java)
        Mockito.`when`(firebaseFirestore.collection("users")).thenReturn(userRef)
        Mockito.`when`(firebaseFirestore.collection("update")).thenReturn(updateRef)
        Mockito.`when`(firebaseFirestore.collection("leaderboard")).thenReturn(leaderBoardRef)
        Mockito.`when`(firebaseStorage.getReference("users")).thenReturn(userStorage)
        profileRepository = ProfileRepositoryImpl(
            userRef = userRef,
            updateRef = updateRef,
            leaderBoardRef = leaderBoardRef,
            userStorage = userStorage
        )
    }

    @Test
    fun `update user profile without username throws error`() = runBlocking {
        profileRepository.updateUser(
            user = User(),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Username cannot be empty.")
            }
        )
    }

    @Test
    fun `update user profile with invalid username throws error`() = runBlocking {
        profileRepository.updateUser(
            user = User(username = "test 123"),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Invalid username.")
            }
        )
    }

    @Test
    fun `update user profile without name throws error`() = runBlocking {
        profileRepository.updateUser(
            user = User(username = "test_123"),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Name cannot be empty.")
            }
        )
    }

    @Test
    fun `update user profile without bio throws error`() = runBlocking {
        profileRepository.updateUser(
            user = User(
                username = "test_123",
                name = "Test"
            ),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Please add a bio.")
            }
        )
    }

    @Test
    fun `update user profile without mentor throws error`() = runBlocking {
        profileRepository.updateUser(
            user = User(
                username = "test_123",
                name = "Test",
                bio = "This is a test bio",
                userType = "student"
            ),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Please add a mentor.")
            }
        )
    }

    @Test
    fun `change password without password throws error`() = runBlocking {
        profileRepository.changePassword(
            password = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Password cannot be empty.")
            }
        )
    }
}