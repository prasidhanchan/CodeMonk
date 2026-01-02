/*
 * Copyright © 2026 Prasidh Gopal Anchan
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

package com.mca.leaderboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.leaderboard.UiState
import com.mca.leaderboard.component.CMBottomSheet
import com.mca.leaderboard.component.TopMemberCard
import com.mca.leaderboard.component.TopMembers
import com.mca.ui.R
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.Loader
import com.mca.ui.theme.Black
import com.mca.util.model.User

@Composable
internal fun LeaderBoardScreen(
    uiState: UiState,
    onCardClick: (username: String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.leaderboard),
                enableBackButton = false
            )
            if (uiState.topMembers.isNotEmpty() && uiState.topMembers.size > 2) {
                TopMembers(
                    topMembers = uiState.topMembers,
                    modifier = Modifier.fillMaxHeight(0.35f)
                )
            }
            CMBottomSheet(modifier = Modifier.fillMaxHeight()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(all = 20.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    itemsIndexed(
                        key = { _, member -> member.userId },
                        items = uiState.topMembers
                    ) { index, member ->
                        TopMemberCard(
                            topMember = member,
                            position = index + 1,
                            onCardClick = onCardClick
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    Loader(loading = uiState.loading)
}

@Preview
@Composable
private fun LeaderBoardScreenPreview() {
    LeaderBoardScreen(
        uiState = UiState(
            topMembers = listOf(
                User(
                    userId = "1",
                    name = "Prasidh Gopal Anchan",
                    xp = 20
                ),
                User(
                    userId = "2",
                    name = "Shahiz Moidin",
                    xp = 18
                ),
                User(
                    userId = "3",
                    name = "Shathwik Shetty",
                    xp = 15
                ),
            )
        ),
        onCardClick = { }
    )
}