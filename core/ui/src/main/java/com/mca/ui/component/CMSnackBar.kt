/*
 * Copyright © 2026 Prasidh Gopal Anchan
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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.R
import com.mca.ui.theme.Green
import com.mca.ui.theme.SnackBarColor
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import kotlinx.coroutines.delay

/**
 * Custom SnackBar with animation.
 */
@Composable
fun CMSnackBar(
    message: String?,
    visible: Boolean,
    modifier: Modifier = Modifier,
    duration: Long = 2000L,
    iconColor: Color,
    onFinish: () -> Unit = { }
) {
    var isVisible by remember(visible) { mutableStateOf(visible) }
    LaunchedEffect(key1 = visible) {
        delay(duration)
        isVisible = false
        delay(500L)
        onFinish()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 200),
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 200),
            targetOffsetY = { it }
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(all = 10.dp)
                .imePadding()
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .then(modifier),
            shape = RoundedCornerShape(10.dp),
            color = SnackBarColor.copy(alpha = 0.95f)
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = message,
                    tint = iconColor
                )
                Spacer(modifier = Modifier.width(10.dp))
                message?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosis,
                            color = fontColor
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CMSnackBarPreview() {
    CMSnackBar(
        message = "Account successfully created!",
        visible = true,
        duration = 2L,
        iconColor = Green
    )
}