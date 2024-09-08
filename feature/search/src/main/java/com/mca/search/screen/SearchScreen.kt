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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.search.UiState
import com.mca.ui.R
import com.mca.ui.component.CMProfileCard
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.CMTextBox
import com.mca.ui.theme.Black
import com.mca.ui.theme.tintColor
import com.mca.util.model.User

@Composable
internal fun SearchScreen(
    uiState: UiState,
    onProfileClick: (username: String) -> Unit,
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.search),
                onBackClick = onBackClick
            )
            CMTextBox(
                value = uiState.search,
                onValueChange = onSearchChange,
                placeHolder = stringResource(id = R.string.search),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = stringResource(id = R.string.search),
                        tint = tintColor
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(
                    key = { _, user -> user.userId },
                    items = uiState.users
                ) { index, user ->
                    CMProfileCard(
                        user = user,
                        delay = 200 * index,
                        onClick = onProfileClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        uiState = UiState(
            users = listOf(
                User(
                    username = "pra_sidh_22",
                    name = "Prasidh Gopal Anchan",
                    userId = "1",
                    bio = "Android Developer | Kotlin | Compose"
                ),
                User(
                    username = "naruto",
                    name = "Uzumaki Naruto",
                    userId = "2",
                    bio = " 7th Hokage of Konohagakure"
                )
            )
        ),
        onProfileClick = { },
        onSearchChange = { },
        onBackClick = { }
    )
}