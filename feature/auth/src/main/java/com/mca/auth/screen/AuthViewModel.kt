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
        password: String
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.login(
                email = email,
                password = password,
                onSuccess = { response ->
                    uiState.update {
                        it.copy(
                            response = response,
                            loading = false
                        )
                    }
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
        uiState.update { it.copy(loading = true) }
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