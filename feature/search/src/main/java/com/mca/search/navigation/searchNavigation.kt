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

package com.mca.search.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mca.search.screen.SearchScreen
import com.mca.search.screen.SearchViewModel
import com.mca.util.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun NavGraphBuilder.searchNavigation(
    navHostController: NavHostController
) {
    composable<Route.Search> {
        val viewModel: SearchViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        val scope = rememberCoroutineScope()

        SearchScreen(
            uiState = uiState,
            onProfileClick = { username ->
                navHostController.navigate(Route.ViewProfile(username))
            },
            onSearchChange = { search ->
                viewModel.setSearch(search)
                scope.launch {
                    viewModel.getSearchUser(search)
                    delay(2000L)
                }
            },
            onBackClick = { navHostController.popBackStack() }
        )
    }
}