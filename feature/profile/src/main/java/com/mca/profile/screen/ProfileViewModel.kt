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
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.model.User
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        getUpdate()
    }

    fun getUser() {
        if (currentUser != null) {
            uiState.update { it.copy(loading = true) }
            viewModelScope.launch(Dispatchers.IO) {
                delay(1000L)
                val result = profileRepository.getUser(currentUser.uid)

                withContext(Dispatchers.Main) {
                    if (result.data != null && !result.loading!! && result.exception == null) {
                        uiState.update {
                            it.copy(
                                currentUser = result.data!!,
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
                        uiState.update { it.copy(loading = result.loading!!) }
                    }
                }
            }
        }
    }

    fun updateUser(onSuccess: () -> Unit) {
        uiState.update { it.copy(loading = true) }
        // Update user profile image url
        updateImageUrl(
            user = uiState.value.currentUser,
            onSuccess = { url ->
                val updatedUser =
                    uiState.value.currentUser.copy(
                        username = uiState.value.currentUser.username,
                        profileImage = url.ifEmpty { uiState.value.currentUser.profileImage }
                    )

                // Update User
                viewModelScope.launch(Dispatchers.IO) {
                    profileRepository.updateUser(
                        user = updatedUser,
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
        )
    }

    private fun updateImageUrl(
        user: User,
        onSuccess: (url: String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedUser = user.copy(profileImage = uiState.value.currentUser.profileImage)
            profileRepository.updateImageUrl(
                user = updatedUser,
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

    fun removeProfileImage(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.removeProfilePic(
                user = uiState.value.currentUser,
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

    fun changePassword(
        password: String,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.changePassword(
                password = password,
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

    private fun getUpdate() {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getUpdate()

            withContext(Dispatchers.Main) {
                if (result.data != null && !result.loading!! && result.exception == null) {
                    uiState.update {
                        it.copy(
                            update = result.data!!,
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

    fun getSelectedUser(
        username: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getSelectedUser(
                username = username,
                onSuccess = onSuccess,
                onError = onError
            )

            withContext(Dispatchers.Main) {
                if (result.data != null && result.exception == null && !result.loading!!) {
                    uiState.update {
                        it.copy(
                            selectedUser = result.data!!,
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

    fun getAllMentors() {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getRandomMentors(uiState.value.selectedUser.userId)

            withContext(Dispatchers.Main) {
                if (result.data != null && result.exception == null && !result.loading!!) {
                    uiState.update {
                        it.copy(
                            otherMentors = result.data!!,
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
                }
            }
        }
    }

    fun getMentorTags(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getMentorTags(username)

            withContext(Dispatchers.Main) {
                if (result.data != null && result.exception == null && !result.loading!!) {
                    uiState.update { it.copy(tags = result.data) }
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

    fun setMentor(mentor: String) {
        uiState.update { it.copy(currentUser = it.currentUser.copy(mentor = mentor)) }
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

    fun setPassword(password: String) = uiState.update { it.copy(newPassword = password) }

    fun clearSelectedUser() = uiState.update { it.copy(selectedUser = User()) }

    fun clearPassword() {
        viewModelScope.launch {
            delay(200L)
            uiState.update { it.copy(newPassword = "") }
        }
    }

    private fun clearUiState() = uiState.update { UiState() }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}