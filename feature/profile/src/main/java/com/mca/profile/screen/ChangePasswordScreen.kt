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

package com.mca.profile.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.profile.UiState
import com.mca.ui.R
import com.mca.ui.component.CMButton
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.CMTextBox
import com.mca.ui.theme.Black
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor

@Composable
internal fun ChangePasswordScreen(
    uiState: UiState,
    onPasswordChange: (String) -> Unit,
    onChangePasswordClick: () -> Unit,
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.change_password),
                onBackClick = onBackClick
            )
            CMTextBox(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.newPassword,
                onValueChange = onPasswordChange,
                placeHolder = stringResource(id = R.string.password),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password),
                        contentDescription = stringResource(id = R.string.password),
                        tint = tintColor
                    )
                },
                keyboardType = KeyboardType.Password,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (uiState.newPassword.isNotBlank()) focusManager.clearFocus()
                        onChangePasswordClick()
                    }
                ),
                capitalization = KeyboardCapitalization.None,
                enableHeader = false,
                autoFocus = true
            )
            CMButton(
                text = stringResource(id = R.string.change_password),
                onClick = {
                    if (uiState.newPassword.isNotBlank()) focusManager.clearFocus()
                    onChangePasswordClick()
                },
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                enabled = !uiState.loading,
                loading = uiState.loading
            )
            Text(
                text = stringResource(id = R.string.recent_login_note),
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor.copy(alpha = 0.5f)
                )
            )
        }
    }

    BackHandler(onBack = onBackClick)
}

@Preview
@Composable
private fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(
        uiState = UiState(),
        onPasswordChange = { },
        onChangePasswordClick = { },
        onBackClick = { }
    )
}