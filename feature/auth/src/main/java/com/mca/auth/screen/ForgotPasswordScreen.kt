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

package com.mca.auth.screen

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.auth.UiState
import com.mca.ui.R
import com.mca.ui.component.CMButton
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.CMTextBox
import com.mca.ui.theme.Black
import com.mca.ui.theme.tintColor

@Composable
internal fun ForgotPasswordScreen(
    uiState: UiState,
    onFindAccountClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val localKeyboard = LocalSoftwareKeyboardController.current
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
                .padding(all = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.forgot_password_heading),
                onBackClick = onBackClick
            )
            CMTextBox(
                modifier = Modifier
                    .focusable(enabled = true)
                    .focusRequester(focusRequester),
                value = uiState.email,
                onValueChange = onEmailChange,
                placeHolder = stringResource(id = R.string.email),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = stringResource(id = R.string.email),
                        tint = tintColor
                    )
                },
                imeAction = ImeAction.Go,
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions(
                    onGo = {
                        focusManager.clearFocus()
                        onFindAccountClick()
                    }
                ),
                capitalization = KeyboardCapitalization.None,
                enableHeader = false
            )
            CMButton(
                text = stringResource(id = R.string.find_account),
                modifier = Modifier.padding(vertical = 20.dp),
                onClick = {
                    localKeyboard?.hide()
                    onFindAccountClick()
                }
            )
        }
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        uiState = UiState(),
        onFindAccountClick = { },
        onEmailChange = { },
        onBackClick = { }
    )
}