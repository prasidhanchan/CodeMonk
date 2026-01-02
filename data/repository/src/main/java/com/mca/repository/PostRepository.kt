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

package com.mca.repository

import android.content.Context
import com.mca.util.model.Post
import com.mca.util.model.Tag
import com.mca.util.warpper.DataOrException
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    suspend fun upsertPost(post: Post, onSuccess: () -> Unit, onError: (String) -> Unit)

    suspend fun addAnnouncement(post: Post, context: Context, onSuccess: () -> Unit, onError: (String) -> Unit)

    suspend fun getPost(postId: String): Flow<DataOrException<Post, Boolean, Exception>>

    suspend fun getTags(username: String): DataOrException<List<Tag>, Boolean, Exception>
}