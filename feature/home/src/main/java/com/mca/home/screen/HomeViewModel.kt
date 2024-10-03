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

package com.mca.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mca.home.UiState
import com.mca.notification.service.NotificationHelper.Companion.clearToken
import com.mca.notification.service.NotificationHelper.Companion.getToken
import com.mca.repository.HomeRepository
import com.mca.repository.NotificationRepository
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.model.User
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    private val currentUser = FirebaseAuth.getInstance().currentUser

    init {
        getPosts()
        upsertToken()
    }

    private fun getPosts() {
        if (currentUser != null) {
            uiState.update { it.copy(loading = true) }
            viewModelScope.launch(Dispatchers.IO) {
                val resultFlow = homeRepository.getPosts()

                withContext(Dispatchers.Main) {
                    resultFlow.distinctUntilChanged()
                        .collectLatest { result ->
                            if (result.data != null && result.exception == null && !result.loading!!) {
                                uiState.update {
                                    it.copy(
                                        posts = result.data?.map { post ->
                                            val user = homeRepository.getUserDetail(post.userId)

                                            val likes: MutableList<String> =
                                                mutableListOf() // Likes list of usernames converted from userIds
                                            likes.addAll(post.likes)
                                            val userId1 = likes.getOrNull(0)
                                            val userId2 = likes.getOrNull(1)
                                            val usernames = homeRepository.getUsername(
                                                userId1 = userId1,
                                                userId2 = userId2
                                            ).data

                                            // Replace first 2 userIds with usernames
                                            if (!usernames.isNullOrEmpty()) {
                                                if (!userId1.isNullOrEmpty()) {
                                                    likes.replaceAll { id ->
                                                        id.replace(
                                                            userId1,
                                                            usernames.getOrElse(
                                                                index = 0,
                                                                defaultValue = { "" }
                                                            )
                                                        )
                                                    }
                                                }
                                                if (!userId2.isNullOrEmpty()) {
                                                    likes.replaceAll { id ->
                                                        id.replace(
                                                            userId2,
                                                            usernames.getOrElse(
                                                                index = 1,
                                                                defaultValue = { "" }
                                                            )
                                                        )
                                                    }
                                                }
                                            }

                                            if (user.data != null && user.exception == null && !user.loading!!) {
                                                // .apply { likes.toList() } Updated likes list with usernames
                                                post.apply { this.likes = likes } to user.data!!
                                            } else {
                                                post to User(username = "cm_user")
                                            }
                                        }.orEmpty(),
                                        loading = false
                                    )
                                }
                            } else {
                                showSnackBar(
                                    response = Response(
                                        message = result.exception?.localizedMessage,
                                        responseType = ResponseType.ERROR
                                    )
                                )
                            }
                        }
                }
            }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.deletePost(
                postId = postId,
                onError = { error ->
                    showSnackBar(
                        response = Response(
                            message = error,
                            responseType = ResponseType.ERROR
                        )
                    )
                }
            )
        }
    }

    fun like(
        postId: String,
        onSuccess: () -> Unit,
        currentUserId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.like(
                postId = postId,
                currentUserId = currentUserId,
                onSuccess = onSuccess,
                onError = { error ->
                    showSnackBar(
                        response = Response(
                            message = error,
                            responseType = ResponseType.ERROR
                        )
                    )
                }
            )
        }
    }

    fun unLike(postId: String, currentUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.unLike(
                postId = postId,
                currentUserId = currentUserId,
                onError = { error ->
                    showSnackBar(
                        response = Response(
                            message = error,
                            responseType = ResponseType.ERROR
                        )
                    )
                }
            )
        }
    }

    private fun upsertToken() {
        val newToken = getToken()
        if (newToken != null) {
            viewModelScope.launch(Dispatchers.IO) {
                notificationRepository.upsertToken(
                    newToken = newToken,
                    userId = currentUser?.uid!!,
                    onSuccess = { clearToken() },
                    onError = { error ->
                        showSnackBar(
                            response = Response(
                                message = error,
                                responseType = ResponseType.ERROR
                            )
                        )
                    }
                )
            }
        }
    }

    private fun clearUiState() = uiState.update { UiState() }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}