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

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import com.mca.repository.SearchRepository
import com.mca.util.constant.matchUsernameAndName
import com.mca.util.model.User
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    val userRef: CollectionReference
) : SearchRepository {

    override suspend fun getAllUsers(search: String): DataOrException<List<User>?, Boolean, Exception> {
        val dataOrException: DataOrException<List<User>?, Boolean, Exception> =
            DataOrException(loading = true)

        try {
            userRef.get()
                .addOnSuccessListener { querySnap ->
                    dataOrException.data = querySnap.documents.mapNotNull { docSnap ->
                        docSnap.toObject<User>()
                    }
                        .filter { user -> user.matchUsernameAndName(search) }
                    when {
                        dataOrException.data?.isEmpty() == true -> dataOrException.data = null
                        search.isBlank() -> dataOrException.data = emptyList()
                    }
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