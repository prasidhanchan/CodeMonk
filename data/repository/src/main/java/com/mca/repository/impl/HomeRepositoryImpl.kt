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

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.CollectionReference
import com.mca.repository.HomeRepository
import com.mca.util.model.Post
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    val postDB: DatabaseReference,
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
                            data = snapshot.children.map { dataSnap ->
                                dataSnap.getValue<Post>()!!
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
}