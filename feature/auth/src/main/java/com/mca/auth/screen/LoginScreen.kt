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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.auth.UiState
import com.mca.ui.R
import com.mca.ui.component.CMButton
import com.mca.ui.component.CMTextBox
import com.mca.ui.theme.Black
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor

@Composable
internal fun LoginScreen(
    uiState: UiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(all = 15.dp)
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.codemonk_logo),
                    contentDescription = stringResource(id = R.string.code_monk),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .size(80.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.code_monk),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                )
            )
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .height(300.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CMTextBox(
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
                    keyboardType = KeyboardType.Email,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    capitalization = KeyboardCapitalization.None,
                    autoFocus = true
                )
                CMTextBox(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    placeHolder = stringResource(id = R.string.password),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.password),
                            contentDescription = stringResource(id = R.string.password),
                            tint = tintColor
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = if (showPassword) R.drawable.eye_crossed else R.drawable.eye),
                            contentDescription = stringResource(id = R.string.show_hide_password),
                            tint = tintColor,
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = { showPassword = !showPassword }
                            )
                        )
                    },
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (uiState.email.isNotBlank() && uiState.password.isNotBlank()) {
                                focusManager.clearFocus()
                            }
                            onLoginClick ()
                        }
                    ),
                    capitalization = KeyboardCapitalization.None,
                    isPassword = true,
                    showPassword = showPassword
                )
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = LinkBlue
                    ),
                    modifier = Modifier.clickable(
                        onClick = onForgotPasswordClick
                    )
                )
                CMButton(
                    text = stringResource(id = R.string.login),
                    modifier = Modifier.padding(vertical = 20.dp),
                    enabled = !uiState.loading,
                    loading = uiState.loading,
                    onClick = {
                        focusManager.clearFocus()
                        onLoginClick()
                    }
                )

            }

            Box(
                modifier= Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.dont_have_account))
                        append(" ")
                        withLink(
                            LinkAnnotation.Clickable(
                                tag = "SignUp",
                                styles = TextLinkStyles(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = LinkBlue
                                    )
                                ),
                                linkInteractionListener = { onSignUpClick() }
                            )
                        ) {
                            append(stringResource(R.string.sign_up))
                        }
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = dosis,
                        color = fontColor
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        uiState = UiState(),
        onEmailChange = { },
        onPasswordChange = { },
        onLoginClick = { },
        onForgotPasswordClick = { },
        onSignUpClick = { }
    )
}