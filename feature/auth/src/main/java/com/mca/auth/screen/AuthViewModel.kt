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

package com.mca.auth.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.auth.UiState
import com.mca.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    /**
     * Function to login into firebase auth.
     */
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        uiState.update {
            it.copy(
                loading = true,
                response = null
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.login(
                email = email,
                password = password,
                onSuccess = {
                    uiState.update { it.copy(loading = false) }
                    onSuccess()
                },
                onError = { response ->
                    uiState.update {
                        it.copy(
                            response = response,
                            loading = false
                        )
                    }
                }
            )
        }
    }

    fun forgotPassword(email: String) {
        uiState.update {
            it.copy(
                loading = true,
                response = null
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.forgotPassword(
                email = email,
                onSuccess = { success ->
                    uiState.update {
                        it.copy(
                            response = success,
                            loading = false
                        )
                    }
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            response = error,
                            loading = false
                        )
                    }
                }
            )
        }
    }

    fun setEmail(value: String) = uiState.update { it.copy(email = value) }

    fun setPassword(value: String) = uiState.update { it.copy(password = value) }

    fun clearMessage() = uiState.update { it.copy(response = null) }

    private fun clearUiState() = uiState.update { UiState() }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}