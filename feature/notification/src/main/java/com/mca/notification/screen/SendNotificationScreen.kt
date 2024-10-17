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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.notification.UiState
import com.mca.ui.R
import com.mca.ui.component.CMButton
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.CMTextBox
import com.mca.ui.theme.Black
import com.mca.ui.theme.tintColor

@Composable
internal fun SendNotificationScreen(
    uiState: UiState,
    onSendClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.send_notification),
                onBackClick = onBackClick
            )
            CMTextBox(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.title,
                onValueChange = onTitleChange,
                placeHolder = stringResource(id = R.string.title),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.title),
                        contentDescription = stringResource(id = R.string.title),
                        tint = tintColor
                    )
                },
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                ),
                singleLine = false,
                maxLines = 4,
                enableHeader = false,
                autoFocus = true
            )
            CMTextBox(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.message,
                onValueChange = onMessageChange,
                placeHolder = stringResource(id = R.string.body),
                imeAction = ImeAction.Default,
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (uiState.title.isNotBlank() &&
                            uiState.message.isNotBlank()
                        ) {
                            focusManager.clearFocus()
                        }
                        onSendClick()
                    }
                ),
                singleLine = false,
                maxLines = 20,
                enableHeader = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            CMButton(
                text = stringResource(id = R.string.send),
                onClick = {
                    if (uiState.title.isNotBlank() &&
                        uiState.message.isNotBlank()
                    ) {
                        focusManager.clearFocus()
                    }
                    onSendClick()
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview
@Composable
private fun SendNotificationScreenPreview() {
    SendNotificationScreen(
        uiState = UiState(),
        onSendClick = { },
        onTitleChange = { },
        onMessageChange = { },
        onBackClick = { }
    )
}