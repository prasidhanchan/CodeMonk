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

package com.mca.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.home.UiState
import com.mca.repository.HomeRepository
import com.mca.util.constants.SnackBarHelper.Companion.showSnackBar
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
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    init {
        getPosts()
    }

    private fun getPosts() {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val resultFlow = homeRepository.getPosts()

            delay(4000L)
            withContext(Dispatchers.Main) {
                resultFlow.distinctUntilChanged()
                    .collectLatest { result ->
                        if (result.exception == null && !result.loading!!) {
                            uiState.update {
                                it.copy(
                                    posts = result.data ?: emptyList(),
                                    loading = result.loading!!
                                )
                            }
                        } else if (result.exception != null) {
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

}