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

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.mca.repository.LeaderBoardRepository
import com.mca.util.model.User
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LeaderBoardRepositoryImpl @Inject constructor(
    val userRef: CollectionReference
) : LeaderBoardRepository {

    override suspend fun getTopMembers(): DataOrException<List<User>, Boolean, Exception> {
        val dataOrException: DataOrException<List<User>, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            userRef
                .whereEqualTo("userType", "student")
                .orderBy("xp", Query.Direction.DESCENDING).get()
                .addOnSuccessListener { querySnap ->
                    dataOrException.data = querySnap.documents.mapNotNull { docSnap ->
                        docSnap.toObject<User>()
                    }
                        .take(10)
                }
                .addOnFailureListener { error ->
                    dataOrException.exception = error
                    Log.d("ERORRR", "getTopMembers: ${error.localizedMessage}")
                }
                .await()
                .asFlow()
        } catch (e: Exception) {
            dataOrException.exception = e
            Log.d("ERORRR", "getTopMembers2: ${e.localizedMessage}")
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }
}