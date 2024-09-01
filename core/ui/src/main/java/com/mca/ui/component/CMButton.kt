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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor

/**
 * Custom Button composable with circular progress indicator.
 */
@Composable
fun CMButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fonSize: Int = 18,
    color: Color = BrandColor,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(52.dp)
            .width(320.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .semantics { contentDescription = text },
        shape = RoundedCornerShape(10.dp),
        color = color
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (!loading) {
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = fonSize.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = fontColor
                    )
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    strokeWidth = 3.5.dp,
                    color = fontColor,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Preview
@Composable
private fun CMButtonPreview() {
    CMButton(
        text = "Login",
        onClick = { }
    )
}