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

package com.mca.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.R
import com.mca.ui.component.CMProgressBar
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor

/**
 * Progress Type for Profile Progress Composable.
 */
internal enum class ProgressType {
    XP,
    ATTENDANCE
}

/**
 * Profile Progress Composable to display progress for XP, Attendance and more.
 */
@Composable
internal fun ProfileProgress(
    icon: Painter,
    tint: Color,
    progress: Int,
    progressType: ProgressType,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Row(
        modifier = modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier.padding(end = 10.dp),
            tint = tint
        )
        CMProgressBar(
            progress = (progress / 100f),
            modifier = Modifier.weight(1f),
            color = tint
        )
        Text(
            text = if (progressType == ProgressType.XP) "$progress pts" else "${progress}%",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor
            ),
            modifier = Modifier
                .padding(start = 10.dp)
                .width(38.dp)
        )
    }
}

@Preview
@Composable
private fun ProfileProgressPreview() {
    ProfileProgress(
        icon = painterResource(id = R.drawable.xp_icon),
        tint = Yellow,
        progress = 20,
        progressType = ProgressType.ATTENDANCE
    )
}