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

import android.content.Context
import androidx.core.net.toUri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import com.mca.repository.PostRepository
import com.mca.util.constant.Constant.DEADLINE_REGEX
import com.mca.util.constant.compressImage
import com.mca.util.constant.convertToMap
import com.mca.util.constant.matchUsername
import com.mca.util.constant.toPostId
import com.mca.util.constant.trimAll
import com.mca.util.model.Post
import com.mca.util.model.Tag
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

class PostRepositoryImpl @Inject constructor(
    val postDB: DatabaseReference,
    val userRef: CollectionReference,
    val postStorage: StorageReference
) : PostRepository {

    override suspend fun upsertPost(
        post: Post,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (post.currentProject.isBlank()) throw Exception("Current project cannot be empty.")
            if (post.teamMembers.size <= 1) throw Exception("Team members cannot be empty.")
            if (post.projectProgress > 100) throw Exception("Project progress cannot be more than 100.")
            if (post.description.length > 200) throw Exception("Description cannot be more than 200 characters.")
            if (post.deadline.isNotBlank() && !post.deadline.matches(DEADLINE_REGEX))
                throw Exception("Deadline should be of format dd MMM yyyy.")

            postDB.child(post.toPostId())
                .updateChildren(post.convertToMap())
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

    override suspend fun addAnnouncement(
        post: Post,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (post.description.isBlank()) throw Exception("Description cannot be empty.")
            if (post.description.length > 300) throw Exception("Your description exceeds 300 characters.")
            if (post.images.size > 4) throw Exception("You can only select up to 4 images.")

            val images: MutableList<String> = mutableListOf()
            if (post.images.isNotEmpty()) {
                // Get download urls for all images
                post.images.forEach { image ->
                    val imageData = compressImage(
                        context = context,
                        uri = image.toUri()
                    )

                    val storageRef = postStorage
                        .child(post.toPostId())
                        .child("image-${Random.nextInt(0, Int.MAX_VALUE)}.${imageData?.mimeType}")

                    if (imageData != null) {
                        storageRef.putBytes(imageData.image!!).await()

                        storageRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                images.add(uri.toString())
                            }
                            .addOnFailureListener { error ->
                                error.localizedMessage?.let(onError)
                            }
                            .await()
                    }
                }
            }

            delay(800L) // Delay so that all the images are added to the list
            // Add to Realtime DB with download urls
            postDB.child(post.toPostId())
                .updateChildren(
                    post.apply { this.images = images.toList() }
                        .trimAll()
                        .convertToMap()
                ).addOnSuccessListener {
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

    override suspend fun getPost(postId: String): Flow<DataOrException<Post, Boolean, Exception>> {
        val dataOrException: MutableStateFlow<DataOrException<Post, Boolean, Exception>> =
            MutableStateFlow(DataOrException(loading = true))

        try {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dataOrException.update {
                        it.copy(
                            data = snapshot.child(postId).getValue<Post>()
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    dataOrException.update { it.copy(exception = error.toException()) }
                }
            }
            postDB.addListenerForSingleValueEvent(valueEventListener)
        } catch (e: Exception) {
            dataOrException.update { it.copy(exception = e) }
        } finally {
            dataOrException.update { it.copy(loading = false) }
        }
        return dataOrException.asStateFlow()
    }

    override suspend fun getTags(username: String): DataOrException<List<Tag>, Boolean, Exception> {
        val dataOrException: DataOrException<List<Tag>, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            userRef.get()
                .addOnSuccessListener { querySnap ->
                    dataOrException.data = querySnap.documents.mapNotNull { docSnap ->
                        docSnap.toObject<Tag>()
                    }
                        .filter { tag -> tag.username.matchUsername(username) }
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
}