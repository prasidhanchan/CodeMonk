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

package com.mca.notification.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mca.notification.UiState
import com.mca.notification.component.NotificationCard
import com.mca.ui.R
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.EmptyResponseIndicator
import com.mca.ui.theme.tintColor
import com.mca.util.constant.Constant.ADMIN
import com.mca.util.model.NotificationData

@Composable
internal fun NotificationScreen(
    uiState: UiState,
    userType: String,
    onSendNotificationClick: () -> Unit,
    updateLastSeen: () -> Unit
) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CMRegularAppBar(
                text = stringResource(id = R.string.notification),
                enableBackButton = false,
                trailingIcon = {
                    if (userType == ADMIN) {
                        Icon(
                            painter = painterResource(id = R.drawable.body),
                            contentDescription = stringResource(id = R.string.send),
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember(::MutableInteractionSource),
                                onClick = onSendNotificationClick
                            ),
                            tint = tintColor
                        )
                    }
                }
            )
        }

        if (uiState.notifications.isNotEmpty() && !uiState.loading) {
            itemsIndexed(
                key = { _, notification -> notification.id },
                items = uiState.notifications
            ) { index, notification ->
                NotificationCard(
                    notification = notification,
                    delay = index * 100
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    EmptyResponseIndicator(
        visible = uiState.notifications.isEmpty(),
        message = stringResource(id = R.string.no_notifications)
    )

    DisposableEffect(key1 = lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START, Lifecycle.Event.ON_DESTROY -> {
                    updateLastSeen()
                }

                else -> Unit
            }
        }

        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Preview
@Composable
private fun NotificationScreenPreview() {
    NotificationScreen(
        uiState = UiState(
            notifications = listOf(
                NotificationData(
                    id = "1",
                    title = "New event",
                    body = "There will be a hackathon this saturday, interested can register @codemonk.club",
                    timeStamp = 1794092800000L
                ),
                NotificationData(
                    id = "2",
                    title = "Laptop remainder",
                    body = "Don't forget to bring your laptop this saturday",
                    timeStamp = 1694092800000L
                )
            )
        ),
        userType = ADMIN,
        onSendNotificationClick = { },
        updateLastSeen = { }
    )
}