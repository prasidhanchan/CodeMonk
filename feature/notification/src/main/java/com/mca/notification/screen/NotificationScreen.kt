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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.notification.UiState
import com.mca.notification.component.NotificationCard
import com.mca.ui.R
import com.mca.ui.component.CMRegularAppBar
import com.mca.util.model.Notification

@Composable
internal fun NotificationScreen(
    uiState: UiState
) {
    LazyColumn(
        modifier= Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CMRegularAppBar(
                text = stringResource(id = R.string.notification),
                enableBackButton = false
            )
        }

        items(
            key = { notification -> notification.id },
            items = uiState.notifications
        ) { notification ->
            NotificationCard(notification = notification)
        }
    }
}

@Preview
@Composable
private fun NotificationScreenPreview() {
    NotificationScreen(
        uiState = UiState(
            notifications = listOf(
                Notification(
                    id = "1",
                    title = "New event",
                    body = "There will be a hackathon this saturday, interested can register @codemonk.club",
                    timeStamp = 1794092800000L
                ),
                Notification(
                    id = "2",
                    title = "Laptop remainder",
                    body = "Don't forget to bring your laptop this saturday",
                    timeStamp = 1694092800000L
                )
            )
        )
    )
}