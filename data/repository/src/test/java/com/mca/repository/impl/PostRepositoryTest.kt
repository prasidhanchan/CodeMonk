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

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mca.repository.PostRepository
import com.mca.util.model.Post
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class PostRepositoryTest {

    private lateinit var userRef: CollectionReference
    private lateinit var postStorage: StorageReference
    private lateinit var postDB: DatabaseReference
    private lateinit var firebaseFireStore: FirebaseFirestore
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var context: Context
    private lateinit var postRepository: PostRepository

    @Before
    fun setUp() {
        firebaseFireStore = mock(FirebaseFirestore::class.java)
        firebaseDatabase = mock(FirebaseDatabase::class.java)
        firebaseStorage = mock(FirebaseStorage::class.java)
        userRef = mock(CollectionReference::class.java)
        postDB = mock(DatabaseReference::class.java)
        postStorage = mock(StorageReference::class.java)
        context = mock(Context::class.java)
        Mockito.`when`(firebaseFireStore.collection("users")).thenReturn(userRef)
        Mockito.`when`(firebaseDatabase.getReference("posts")).thenReturn(postDB)
        Mockito.`when`(firebaseStorage.getReference("posts")).thenReturn(postStorage)
        postRepository = PostRepositoryImpl(
            postDB = postDB,
            userRef = userRef,
            postStorage = postStorage
        )
    }

    @Test
    fun `upsert post without current project name throws error`() = runBlocking {
        postRepository.upsertPost(
            post = Post(currentProject = ""),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Current project cannot be empty.")
            }
        )
    }

    @Test
    fun `upsert post with less that 2 team members throws error`() = runBlocking {
        postRepository.upsertPost(
            post = Post(
                currentProject = "Test",
                teamMembers = listOf("me")
            ),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Team members cannot be empty.")
            }
        )
    }

    @Test
    fun `upsert post with progress more than 100 throws error`() = runBlocking {
        postRepository.upsertPost(
            post = Post(
                currentProject = "Test",
                teamMembers = listOf("me", "tester_cm"),
                projectProgress = 200
            ),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Project progress cannot be more than 100.")
            }
        )
    }

    @Test
    fun `upsert post with description more than 200 characters throws error`() = runBlocking {
        postRepository.upsertPost(
            post = Post(
                currentProject = "Test",
                teamMembers = listOf("me", "tester_cm"),
                projectProgress = 100,
                description = TestUtil.DESCRIPTION_200
            ),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Description cannot be more than 200 characters.")
            }
        )
    }

    @Test
    fun `upsert post with wrong deadline format throws error`() = runBlocking {
        postRepository.upsertPost(
            post = Post(
                currentProject = "Test",
                teamMembers = listOf("me", "tester_cm"),
                projectProgress = 100,
                description = "This is a test description",
                deadline = "20 April 2024" // Should be 20 Apr 2024
            ),
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Deadline should be of format dd MMM yyyy.")
            }
        )
    }

    @Test
    fun `add announcement without description throws error`() = runBlocking {
        postRepository.addAnnouncement(
            post = Post(description = ""),
            context = context,
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Description cannot be empty.")
            }
        )
    }

    @Test
    fun `add announcement with description more than 300 characters throws error`() = runBlocking {
        postRepository.addAnnouncement(
            post = Post(description = TestUtil.DESCRIPTION_300),
            context = context,
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Your description exceeds 300 characters.")
            }
        )
    }

    @Test
    fun `add announcement with more than 4 images throws error`() = runBlocking {
        postRepository.addAnnouncement(
            post = Post(
                description = "This is a test description",
                images = listOf("1", "2", "3", "4", "5")
            ),
            context = context,
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("You can only select up to 4 images.")
            }
        )
    }
}