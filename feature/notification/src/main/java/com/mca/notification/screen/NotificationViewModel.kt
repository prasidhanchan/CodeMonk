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

package com.mca.notification.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.notification.UiState
import com.mca.repository.NotificationRepository
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.model.PushNotification
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
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    init {
        getNotifications()
    }

    fun getAccessToken(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = notificationRepository.getAccessToken(context)

            withContext(Dispatchers.Main) {
                if (result.data != null && result.exception == null && !result.loading!!) {
                    uiState.update { it.copy(accessToken = result.data) }
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

    fun sendNotification(
        pushNotification: PushNotification,
        accessToken: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepository.sendNotification(
                pushNotification = pushNotification,
                accessToken = accessToken,
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

    private fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.update { it.copy(loading = true) }
            notificationRepository.getNotifications()
                .distinctUntilChanged()
                .collectLatest { result ->
                    withContext(Dispatchers.Main) {
                        if (result.data != null && result.exception == null && !result.loading!!) {
                            uiState.update {
                                it.copy(
                                    notifications = result.data!!,
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

    fun setTitle(title: String) = uiState.update { it.copy(title = title) }

    fun setMessage(message: String) = uiState.update { it.copy(message = message) }
}