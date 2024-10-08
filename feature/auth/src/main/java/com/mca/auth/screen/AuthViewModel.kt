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
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    init {
        getTesters()
    }

    /**
     * Function to login into firebase auth.
     */
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.login(
                email = email,
                password = password,
                onSuccess = {
                    uiState.update { it.copy(loading = false) }
                    onSuccess()
                },
                onError = { response ->
                    showSnackBar(response)
                    uiState.update { it.copy(loading = false) }
                }
            )
        }
    }

    fun forgotPassword(email: String) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.forgotPassword(
                email = email,
                onSuccess = { response ->
                    showSnackBar(response)
                    uiState.update { it.copy(loading = false) }
                },
                onError = { response ->
                    showSnackBar(response)
                    uiState.update { it.copy(loading = false) }
                }
            )
        }
    }

    private fun getTesters() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.getTesters()

            withContext(Dispatchers.Main) {
                if (result.data != null && result.exception == null && !result.loading!!) {
                    uiState.update {
                        it.copy(
                            testers = result.data!!,
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

    fun setEmail(value: String) = uiState.update { it.copy(email = value) }

    fun setPassword(value: String) = uiState.update { it.copy(password = value) }

    fun clearUiState() {
        viewModelScope.launch(Dispatchers.Main) {
            delay(500L)
            uiState.update { UiState() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}