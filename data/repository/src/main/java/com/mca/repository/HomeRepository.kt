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

package com.mca.repository

import com.mca.util.model.Post
import com.mca.util.model.User
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getPosts(): Flow<DataOrException<List<Post>, Boolean, Exception>>

    suspend fun getUsernames(userIds: List<String>): DataOrException<List<String>, Boolean, Exception>

    suspend fun getUserDetail(userId: String): DataOrException<User, Boolean, Exception>

    suspend fun getUsername(
        userId1: String?,
        userId2: String?
    ): DataOrException<List<String>, Boolean, Exception>

    suspend fun deletePost(postId: String, onSuccess: () -> Unit, onError: (String) -> Unit)

    suspend fun like(
        postId: String,
        currentUserId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun unLike(postId: String, currentUserId: String, onError: (String) -> Unit)
}