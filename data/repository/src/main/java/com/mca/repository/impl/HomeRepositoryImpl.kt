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

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import com.mca.repository.HomeRepository
import com.mca.util.model.Post
import com.mca.util.model.User
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    val postDB: DatabaseReference,
    val postStorage: StorageReference,
    val userRef: CollectionReference
) : HomeRepository {

    override suspend fun getPosts(): Flow<DataOrException<List<Post>, Boolean, Exception>> {
        val dataOrException: MutableStateFlow<DataOrException<List<Post>, Boolean, Exception>> =
            MutableStateFlow(DataOrException(loading = true))

        try {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dataOrException.update {
                        it.copy(
                            data = snapshot.children.mapNotNull { dataSnap ->
                                dataSnap.getValue<Post>()
                            }
                                .sortedByDescending { post -> post.timeStamp }
                        )
                    }
                    dataOrException.update { it.copy(loading = false) }
                }

                override fun onCancelled(error: DatabaseError) {
                    dataOrException.update { it.copy(exception = error.toException()) }
                }
            }
            postDB.addValueEventListener(valueEventListener)
        } catch (e: Exception) {
            dataOrException.update { it.copy(exception = e) }
        }
        return dataOrException.asStateFlow()
    }

    override suspend fun getUsernames(userIds: List<String>): DataOrException<List<String>, Boolean, Exception> {
        val dataOrException: DataOrException<List<String>, Boolean, Exception> =
            DataOrException(loading = true)
        val usernames: MutableList<String> = mutableListOf()

        try {
            userIds.forEach { userId ->
                userRef.document(userId).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            usernames.add(task.result.getString("username").orEmpty())
                        }
                    }
                    .await()
            }
        } catch (e: Exception) {
            dataOrException.exception = e
        } finally {
            dataOrException.loading = false
            dataOrException.data = usernames
        }
        return dataOrException
    }

    override suspend fun getUserDetail(userId: String): DataOrException<User, Boolean, Exception> {
        val dataOrException: DataOrException<User, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            userRef.document(userId).get()
                .addOnSuccessListener { docSnap ->
                    dataOrException.data = docSnap.toObject<User>()
                }
                .addOnFailureListener { error ->
                    dataOrException.exception = error
                }
                .await()
        } catch (e: Exception) {
            dataOrException.exception = e
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

    override suspend fun getUsername(
        userId1: String?,
        userId2: String?
    ): DataOrException<List<String>, Boolean, Exception> {
        val dataOrException: DataOrException<List<String>, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            userRef.whereIn(FieldPath.documentId(), listOfNotNull(userId1, userId2)).get()
                .addOnSuccessListener { querySnap ->
                    dataOrException.data = querySnap.documents
                        .mapNotNull { it.toObject<User>()?.username }
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

    override suspend fun deletePost(
        postId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            postDB.child(postId).removeValue()
                .addOnSuccessListener {
                    postStorage.child(postId).listAll()
                        .addOnSuccessListener { result ->
                            result.items.forEach { item ->
                                item.delete()
                            }
                            onSuccess()
                        }
                }
                .addOnFailureListener { error ->
                    error.localizedMessage?.let(onError)
                }
                .await()
        } catch (e: Exception) {
            e.localizedMessage?.let(onError)
        }
    }

    override suspend fun like(
        postId: String,
        currentUserId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        var likes = mutableListOf<String>()

        try {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val snap = snapshot.child(postId).child("likes")
                    if (snap.exists()) {
                        likes = snap.getValue<List<String>>()?.toMutableList()!!
                    }
                    likes.add(currentUserId)
                    postDB.child(postId)
                        .updateChildren(mapOf("likes" to likes))
                        .addOnSuccessListener { onSuccess() }
                }

                override fun onCancelled(error: DatabaseError) {
                    error.toException().localizedMessage?.let(onError)
                }
            }
            postDB.addListenerForSingleValueEvent(valueEventListener)
        } catch (_: Exception) {
            onError("Couldn't like the post")
        }
    }

    override suspend fun unLike(
        postId: String,
        currentUserId: String,
        onError: (String) -> Unit
    ) {
        var likes: MutableList<String>

        try {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val snap = snapshot.child(postId).child("likes")
                    if (snap.exists()) {
                        likes = snap.getValue<List<String>>()?.toMutableList()!!
                        likes.remove(currentUserId)
                        postDB.child(postId).updateChildren(mapOf("likes" to likes))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    error.toException().localizedMessage?.let(onError)
                }
            }
            postDB.addListenerForSingleValueEvent(valueEventListener)
        } catch (_: Exception) {
            onError("Couldn't remove the like")
        }
    }
}