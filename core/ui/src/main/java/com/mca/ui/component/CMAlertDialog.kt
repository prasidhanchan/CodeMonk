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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.theme.Black
import com.mca.ui.theme.ExtraLightBlack
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor

/**
 * Custom Alert Dialog composable with animation.
 */
@Composable
fun CMAlertDialog(
    title: String,
    message: String,
    visible: Boolean,
    confirmText: String,
    dismissText: String,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 250)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 250)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Black.copy(alpha = 0.7f))
                .clickable(
                    enabled = false,
                    onClick = { }
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .fillMaxWidth(0.8f),
                shape = RoundedCornerShape(15.dp),
                color = LightBlack
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosis,
                            color = fontColor,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = message,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = dosis,
                            color = fontColor,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    CMButton(
                        text = confirmText,
                        modifier = Modifier
                            .padding(vertical = 15.dp),
                        onClick = onConfirm
                    )
                    CMButton(
                        text = dismissText,
                        color = ExtraLightBlack,
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0XFFFFFFFF
)
@Composable
private fun CMAlertDialogPreview() {
    CMAlertDialog(
        title = "Logout",
        message = "Are you sure you want to logout?",
        visible = true,
        confirmText = "Confirm",
        dismissText = "Cancel",
        onConfirm = { },
        onDismiss = { }
    )
}