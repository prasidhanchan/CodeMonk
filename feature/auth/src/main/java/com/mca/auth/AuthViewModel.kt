package com.mca.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.login(
                email = email,
                password = password,
                onSuccess = {
                    onSuccess()
                    uiState.update { it.copy(loading = false) }
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            error = error,
                            loading = false
                        )
                    }
                }
            )
        }
    }

    fun setEmail(value: String) = uiState.update { it.copy(email = value) }

    fun setPassword(value: String) = uiState.update { it.copy(password = value) }

    fun clearError() = uiState.update { it.copy(error = "") }

    private fun clearUiState() = uiState.update { UiState() }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}