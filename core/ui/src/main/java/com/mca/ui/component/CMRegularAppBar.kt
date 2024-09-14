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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.R
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor

/**
 * Regular AppBAr composable for all screens.
 */
@Composable
fun CMRegularAppBar(
    text: String,
    modifier: Modifier = Modifier,
    enableBackButton: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    onBackClick: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .padding(
                top = 10.dp,
                bottom = 30.dp,
                start = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        when {
            enableBackButton && trailingIcon != null -> BackButton(onClick = onBackClick)
            enableBackButton -> BackButton(onClick = onBackClick)
            trailingIcon != null -> Spacer(modifier = Modifier.width(36.dp))
        }
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                enableBackButton && trailingIcon != null -> trailingIcon()
                !enableBackButton && trailingIcon == null -> Unit
                trailingIcon != null -> trailingIcon()
                else -> Spacer(modifier = Modifier.width(36.dp))
            }
        }
    }
}

@Preview
@Composable
private fun CmRegularAppbarPreview() {
    CMRegularAppBar(text = "Forgot Password", enableBackButton = false, trailingIcon = { Icon(
        painter = painterResource(id = R.drawable.body),
        contentDescription = "",
        tint = tintColor
    ) })
}