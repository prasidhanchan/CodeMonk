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

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.android.gms.ads.nativead.NativeAd
import com.mca.search.BuildConfig.NATIVE_AD_ID_SEARCH
import com.mca.search.screen.SearchScreen
import com.mca.search.screen.SearchViewModel
import com.mca.util.constant.Constant.IN_OUT_DURATION
import com.mca.util.constant.Constant.MAX_SEARCH_ADS
import com.mca.util.constant.loadNativeAds
import com.mca.util.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun NavGraphBuilder.searchNavigation(
    navHostController: NavHostController
) {
    composable<Route.Search>(
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = IN_OUT_DURATION))
        }
    ) {
        val viewModel: SearchViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val nativeAds = remember { mutableStateListOf<NativeAd?>() }
        LaunchedEffect(key1 = Unit) {
            loadNativeAds(
                context = context,
                adUnitId = NATIVE_AD_ID_SEARCH,
                onAdLoaded = { ad ->
                    nativeAds.add(ad)
                },
                maxAds = MAX_SEARCH_ADS,
                adChoicesPlacement = 1
            )
        }

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
            onBackClick = { navHostController.popBackStack() },
            nativeAds = nativeAds
        )
    }
}