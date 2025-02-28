/*
 * Copyright © 2025 Prasidh Gopal Anchan
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

package com.mca.search.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.repository.SearchRepository
import com.mca.search.UiState
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
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
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    fun getSearchUser(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.update { it.copy(loading = true) }
            viewModelScope.launch(Dispatchers.IO) {
                val result = searchRepository.getAllUsers(search)

                withContext(Dispatchers.Main) {
                    if (result.exception == null && !result.loading!!) {
                        uiState.update {
                            it.copy(
                                users = result.data,
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
                        uiState.update { it.copy(loading = false) }
                    }
                }
            }
        }
    }

    fun setSearch(search: String) {
        uiState.update { it.copy(search = search) }
    }

    fun clearUsers() = uiState.update { it.copy(users = emptyList()) }
}