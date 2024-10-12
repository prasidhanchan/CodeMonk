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

package com.mca.search.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.nativead.NativeAd
import com.mca.search.UiState
import com.mca.search.component.ProfileCardNativeAd
import com.mca.ui.R
import com.mca.ui.component.CMProfileCard
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.CMTextBox
import com.mca.ui.component.EmptyResponseIndicator
import com.mca.ui.theme.Black
import com.mca.ui.theme.tintColor
import com.mca.util.constant.Constant.MAX_SEARCH_ADS

@Composable
internal fun SearchScreen(
    uiState: UiState,
    onProfileClick: (username: String) -> Unit,
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit,
    nativeAds: List<NativeAd?>
) {
    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.search),
                onBackClick = onBackClick
            )
            CMTextBox(
                modifier = Modifier
                    .focusable(enabled = true)
                    .focusRequester(focusRequester),
                value = uiState.search,
                onValueChange = onSearchChange,
                placeHolder = stringResource(id = R.string.search),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = stringResource(id = R.string.search),
                        tint = tintColor
                    )
                },
                imeAction = ImeAction.Search,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (uiState.search.isNotBlank()) focusManager.clearFocus()
                        onSearchChange(uiState.search)
                    }
                ),
                enableHeader = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!uiState.users.isNullOrEmpty()) {
                    itemsIndexed(
                        key = { _, user -> user.userId },
                        items = uiState.users!!
                    ) { index, user ->
                        CMProfileCard(
                            user = user,
                            modifier = Modifier.padding(bottom = 5.dp),
                            delay = index * 100,
                            onClick = onProfileClick
                        )
                        if (index < MAX_SEARCH_ADS && nativeAds.size == MAX_SEARCH_ADS) {
                            ProfileCardNativeAd(
                                nativeAd = nativeAds[index],
                                delay = index * 100
                            )
                        }
                    }
                } else if (uiState.search.isBlank()) {
                    item(key = "searchSomeone") {
                        Image(
                            painter = painterResource(id = R.drawable.search_sccreen_media_dark),
                            contentDescription = stringResource(id = R.string.search_something),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 50.dp)
                        )
                    }
                }
            }
        }
    }

    EmptyResponseIndicator(
        visible = uiState.users == null,
        message = stringResource(id = R.string.monk_not_found)
    )
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        uiState = UiState(
            users = listOf()
        ),
        onProfileClick = { },
        onSearchChange = { },
        onBackClick = { },
        nativeAds = listOf()
    )
}