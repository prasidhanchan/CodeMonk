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

package com.mca.profile.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.profile.UiState
import com.mca.ui.R
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.Loader
import com.mca.ui.theme.Black
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor
import com.mca.util.constant.getCurrentVersion
import com.mca.util.model.Update

@Composable
internal fun AboutScreen(
    uiState: UiState,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.about),
                onBackClick = onBackClick
            )
            Image(
                painter = painterResource(id = R.drawable.codemonk_logo),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            AboutCard(
                text = stringResource(
                    id = R.string.current_version,
                    context.getCurrentVersion(),
                    uiState.update.versionInfo
                ),
                icon = painterResource(id = R.drawable.version),
                enabled = false
            )
            AboutCard(
                text = stringResource(id = R.string.whats_new),
                onClick = { uriHandler.openUri(uiState.update.changelogs) },
                icon = painterResource(id = R.drawable.whats_new)
            )
            AboutCard(
                text = stringResource(id = R.string.download),
                onClick = { uriHandler.openUri(uiState.update.download) },
                icon = painterResource(id = R.drawable.download)
            )
            AboutCard(
                text = stringResource(id = R.string.report),
                onClick = { uriHandler.openUri(uiState.update.report) },
                icon = painterResource(id = R.drawable.report)
            )
            AboutCard(
                text = stringResource(id = R.string.privacy_policy),
                onClick = { uriHandler.openUri(uiState.update.privacyPolicy) },
                icon = painterResource(id = R.drawable.privacy_policy)
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.x),
                    contentDescription = stringResource(id = R.string.x),
                    tint = tintColor,
                    modifier = Modifier.clickable(
                        onClick = { uriHandler.openUri(context.getString(R.string.x_link)) }
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = stringResource(id = R.string.github),
                    tint = tintColor,
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .clickable(
                            onClick = { uriHandler.openUri(context.getString(R.string.github_link)) }
                        )
                )
                Icon(
                    painter = painterResource(id = R.drawable.website),
                    contentDescription = stringResource(id = R.string.website),
                    tint = tintColor,
                    modifier = Modifier.clickable(
                        onClick = { uriHandler.openUri(context.getString(R.string.website_link)) }
                    )
                )
            }
        }
    }

    Loader(loading = uiState.loading)
}

@Composable
private fun AboutCard(
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = { }
) {
    Surface(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        shape = RoundedCornerShape(8.dp),
        color = LightBlack
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                tint = tintColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                ),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    AboutScreen(
        uiState = UiState(update = Update(versionInfo = "Stable")),
        onBackClick = { }
    )
}