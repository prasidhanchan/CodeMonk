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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mca.post.UiState
import com.mca.repository.PostRepository
import com.mca.util.constants.SnackBarHelper.Companion.showSnackBar
import com.mca.util.model.Post
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun upsertPost(
        post: Post,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.upsertPost(
                post = post,
                onSuccess = onSuccess,
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


    fun setCurrentProject(currentProject: String) {
        uiState.update { it.copy(currentProject = currentProject) }
    }

    fun setTeamMembers(teamMembers: List<String>) {
        uiState.update { it.copy(teamMembers = teamMembers) }
    }

    fun setProjectProgress(projectProgress: String) {
        uiState.update { it.copy(projectProgress = projectProgress.toInt()) }
    }

    fun setDeadline(deadline: String) {
        uiState.update { it.copy(deadline = deadline) }
    }

}