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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mca.ui.R
import com.mca.ui.component.CMIconButton
import com.mca.util.model.User

/**
 * Link section for the profile and view profile screen to display the links.
 */
@Composable
internal fun LinkSection(
    user: User,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    if (
        user.linkedInLink.isNotBlank() ||
        user.gitHubLink.isNotBlank() ||
        user.portfolioLink.isNotBlank()
    ) {
        Row(
            modifier = modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (user.linkedInLink.isNotBlank()) {
                CMIconButton(
                    link = user.linkedInLink,
                    icon = painterResource(id = R.drawable.linkedin),
                    onClick = { uriHandler.openUri(user.linkedInLink) },
                    contentDescription = stringResource(id = R.string.linkedin)
                )
            }
            if (user.gitHubLink.isNotBlank()) {
                CMIconButton(
                    link = user.gitHubLink,
                    icon = painterResource(id = R.drawable.github),
                    onClick = { uriHandler.openUri(user.gitHubLink) },
                    contentDescription = stringResource(id = R.string.github)
                )
            }
            if (user.portfolioLink.isNotBlank()) {
                CMIconButton(
                    link = user.portfolioLink,
                    icon = painterResource(id = R.drawable.more_link),
                    onClick = { uriHandler.openUri(user.portfolioLink) },
                    contentDescription = stringResource(id = R.string.more_link)
                )
            }
        }
    }
}