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

package com.mca.post.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.post.UiState
import com.mca.repository.PostRepository
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.model.Post
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    fun upsertPost(
        post: Post,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(updating = true) }
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.upsertPost(
                post = post,
                onSuccess = {
                    onSuccess()
                    uiState.update { it.copy(updating = false) }
                },
                onError = { error ->
                    showSnackBar(
                        response = Response(
                            message = error,
                            responseType = ResponseType.ERROR
                        )
                    )
                    uiState.update { it.copy(updating = false) }
                }
            )
        }
    }

    fun addAnnouncement(
        post: Post,
        context: Context,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.addAnnouncement(
                post = post,
                context = context,
                onSuccess = {
                    onSuccess()
                    uiState.update { it.copy(loading = false) }
                },
                onError = { error ->
                    showSnackBar(
                        response = Response(
                            message = error,
                            responseType = ResponseType.ERROR
                        )
                    )
                    uiState.update { it.copy(loading = false) }
                }
            )
        }
    }

    fun getPost(postId: String) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            postRepository.getPost(postId)
                .distinctUntilChanged()
                .collectLatest { result ->
                    withContext(Dispatchers.Main) {
                        if (result.data != null && result.exception == null && !result.loading!!) {
                            uiState.update {
                                it.copy(
                                    userId = result.data?.userId!!,
                                    currentProject = result.data?.currentProject!!,
                                    teamMembers = result.data?.teamMembers!!,
                                    description = result.data?.description!!,
                                    projectProgress = result.data?.projectProgress.toString(),
                                    deadline = result.data?.deadline!!,
                                    postId = result.data?.postId!!,
                                    timestamp = result.data?.timeStamp!!,
                                    loading = result.loading!!
                                )
                            }
                        } else {
                            showSnackBar(
                                response = Response(
                                    message = result.exception?.localizedMessage,
                                    responseType = ResponseType.ERROR
                                )
                            )
                            uiState.update { it.copy(loading = false) }
                        }
                    }
                }

        }
    }

    fun getTags(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = postRepository.getTags(username)

            withContext(Dispatchers.Main) {
                if (result.data != null && result.exception == null && !result.loading!!) {
                    uiState.update {
                        it.copy(tags = result.data)
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

    fun setCurrentProject(currentProject: String) {
        uiState.update { it.copy(currentProject = currentProject) }
    }

    fun setTeamMembers(teamMembers: List<String>) {
        uiState.update { it.copy(teamMembers = teamMembers) }
    }

    fun setDescription(description: String) {
        uiState.update { it.copy(description = description) }
    }

    fun setProjectProgress(projectProgress: String) {
        uiState.update { it.copy(projectProgress = projectProgress) }
    }

    fun setDeadline(deadline: String) {
        uiState.update { it.copy(deadline = deadline) }
    }

    private fun clearUiState() = uiState.update { UiState() }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}