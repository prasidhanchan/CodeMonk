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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.ui.R
import com.mca.ui.theme.tintColor

/**
 * CMIconButton composable to display icon button to open the link.
 */
@Composable
fun CMIconButton(
    link: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (link.isNotBlank()) {
        Icon(
            painter = icon,
            contentDescription = stringResource(id = R.string.link),
            tint = tintColor,
            modifier = modifier
                .padding(horizontal = 15.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick
                )
        )
    }
}

@Preview
@Composable
private fun CMIconButtonPreview() {
    CMIconButton(
        link = "https://codemonk.club",
        icon = painterResource(id = R.drawable.website),
        onClick = { }
    )
}