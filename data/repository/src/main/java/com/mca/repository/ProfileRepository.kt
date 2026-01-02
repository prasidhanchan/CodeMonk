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

import com.mca.util.model.Tag
import com.mca.util.model.TopMember
import com.mca.util.model.Update
import com.mca.util.model.User
import com.mca.util.warpper.DataOrException

interface ProfileRepository {

    suspend fun getUser(currentUserId: String): DataOrException<User, Boolean, Exception>

    suspend fun updateUser(
        user: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun updateImageUrl(
        user: User,
        onSuccess: (url: String) -> Unit,
        onError: (String) -> Unit
    )

    suspend fun removeProfilePic(
        user: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun logout()

    suspend fun changePassword(password: String, onSuccess: () -> Unit, onError: (String) -> Unit)

    suspend fun getUpdate(): DataOrException<Update, Boolean, Exception>

    suspend fun getSelectedUser(
        userId: String,
        onSuccess: (user: User?) -> Unit,
        onError: (error: String) -> Unit
    )

    suspend fun getRandomMentors(selectedUserId: String): DataOrException<List<User>, Boolean, Exception>

    suspend fun getMentorTags(username: String): DataOrException<List<Tag>, Boolean, Exception>

    suspend fun getTopMembers(): DataOrException<TopMember, Boolean, Exception>

    suspend fun updatePoints(newPoints: Int, userId: String, onSuccess: () -> Unit)
}