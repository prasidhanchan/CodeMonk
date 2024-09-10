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

package com.mca.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor

/**
 * TextBox custom composable for normal text and passwords.
 */
@Composable
fun CMTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions(),
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    enabled: Boolean = true,
    enableHeader: Boolean = true
) {
    Column(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (enableHeader) {
            Text(
                text = placeHolder,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = dosis,
                    color = fontColor.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth()
            )
        }
        TextField(
            value = value,
            onValueChange = { textValue -> onValueChange(textValue) },
            modifier = modifier
                .padding(vertical = 7.dp)
                .wrapContentHeight(Alignment.CenterVertically)
                .defaultMinSize(minHeight = 52.dp)
                .width(320.dp)
                .semantics { contentDescription = placeHolder },
            placeholder = {
                Text(
                    text = placeHolder,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = dosis,
                        color = fontColor.copy(alpha = 0.5f)
                    )
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            enabled = enabled,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LightBlack,
                unfocusedContainerColor = LightBlack,
                focusedTextColor = fontColor,
                unfocusedTextColor = fontColor,
                disabledContainerColor = LightBlack,
                disabledTextColor = fontColor.copy(alpha = 0.5f),
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = dosis,
                color = fontColor
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
                capitalization = capitalization
            ),
            keyboardActions = keyboardActions,
            visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        )
    }
}

@Preview
@Composable
private fun CMTextBoxPreview() {
    CMTextBox(
        value = "Hello",
        onValueChange = { },
        placeHolder = "Email"
    )
}