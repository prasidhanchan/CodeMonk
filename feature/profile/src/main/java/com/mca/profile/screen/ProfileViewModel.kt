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

package com.mca.profile.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mca.profile.UiState
import com.mca.repository.ProfileRepository
import com.mca.util.constants.SnackBarHelper.Companion.showSnackBar
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    private val currentUser = FirebaseAuth.getInstance().currentUser

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getUser(currentUser?.uid!!)

            withContext(Dispatchers.Main) {
                uiState.update { it.copy(loading = true) }
                if (result.data != null && !result.loading!!) {
                    uiState.update {
                        it.copy(
                            currentUser = result.data!!,
                            loading = result.loading!!
                        )
                    }
                } else if (result.exception != null && !result.loading!!) {
                    showSnackBar(
                        response = Response(
                            message = result.exception?.localizedMessage,
                            responseType = ResponseType.ERROR
                        )
                    )
                    uiState.update { it.copy(loading = result.loading!!) }
                } else {
                    uiState.update { it.copy(loading = result.loading!!) }
                }
            }
        }
    }

    fun updateUser(onSuccess: () -> Unit) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.updateUser(
                user = uiState.value.currentUser,
                onSuccess = {
                    onSuccess()
                    uiState.update { it.copy(loading = false) }
                },
                onError = { error ->
                    uiState.update { it.copy(loading = false) }
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

    fun removeProfileImage(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.removeProfilePic(
                currentUserId = currentUser?.uid!!,
                onSuccess = {
                    uiState.update { it.copy(currentUser = it.currentUser.copy(profileImage = "")) }
                    onSuccess()
                },
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

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.logout()
        }
    }

    fun setUsername(username: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(username = username)) }
    }

    fun setName(name: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(name = name)) }
    }

    fun setProfileImage(profileImage: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(profileImage = profileImage)) }
    }

    fun setBio(bio: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(bio = bio)) }
    }

    fun setCurrentProject(currentProject: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(currentProject = currentProject)) }
    }

    fun setLinkedInLink(linkedInLink: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(linkedInLink = linkedInLink)) }
    }

    fun setGitHubLink(gitHubLink: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(gitHubLink = gitHubLink)) }
    }

    fun setPortfolioLink(portfolioLink: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(portfolioLink = portfolioLink)) }
    }

    private fun clearUiState() = uiState.update { UiState() }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}