package com.mca.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onForgotPasswordClick: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val localKeyboard = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(all = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.codemonk_logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier.padding(vertical = 10.dp)
                        .size(80.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                )
            )
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
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
                    keyboardType = KeyboardType.Email
                )
                CMTextBox(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    placeHolder = stringResource(id = R.string.password),
                    isPassword = true,
                    showPassword = showPassword,
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
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            localKeyboard?.hide()
                            onLoginClick ()
                        }
                    )
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
                    onClick = onLoginClick
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
        onForgotPasswordClick = { }
    )
}