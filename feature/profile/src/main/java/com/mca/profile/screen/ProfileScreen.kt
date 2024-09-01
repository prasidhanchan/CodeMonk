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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mca.profile.UiState
import com.mca.profile.component.ProfileActionButton
import com.mca.profile.component.ProfileProgress
import com.mca.profile.component.ProgressType
import com.mca.ui.R
import com.mca.ui.component.CMIconButton
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.Green
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.util.model.User

@Composable
internal fun ProfileScreen(
    uiState: UiState,
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: (
        title: String,
        message: String
    ) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CMRegularAppBar(
            text = stringResource(id = R.string.profile),
            enableBackButton = false
        )
        Surface(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .size(100.dp),
            shape = CircleShape,
            color = LightBlack,
            content = {
                AsyncImage(
                    model = uiState.currentUser.profileImage,
                    contentDescription = stringResource(id = R.string.profile),
                    contentScale = ContentScale.Crop
                )
            }
        )
        Text(
            text = uiState.currentUser.name,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor,
                textAlign = TextAlign.Center
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = uiState.currentUser.bio,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            ),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
        if (uiState.currentUser.linkedInLink.isNotEmpty() ||
            uiState.currentUser.gitHubLink.isNotEmpty() ||
            uiState.currentUser.portfolioLink.isNotEmpty()
        ) {
            LinkSection(
                uiState = uiState,
                onLinkedInClick = { uriHandler.openUri(uiState.currentUser.linkedInLink) },
                onGitHubClick = { uriHandler.openUri(uiState.currentUser.gitHubLink) },
                onPortfolioClick = { uriHandler.openUri(uiState.currentUser.portfolioLink) }
            )
        }

        Surface(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
                .wrapContentSize(Alignment.Center),
            shape = CircleShape,
            color = LightBlack,
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .wrapContentSize(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.username, uiState.currentUser.username),
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                        .padding(bottom = 2.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = fontColor,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                if (uiState.currentUser.isVerified || uiState.currentUser.userType == "Admin") {
                    Icon(
                        painter = painterResource(id = R.drawable.tick),
                        contentDescription = stringResource(id = R.string.blue_tick),
                        modifier = Modifier.padding(bottom = 1.dp),
                        tint = LinkBlue
                    )
                }
            }
        }

        if (uiState.currentUser.userType == "Admin") {
            Text(
                text = stringResource(R.string.mentor, uiState.currentUser.mentor),
                modifier = Modifier.padding(all = 8.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = LinkBlue,
                    textAlign = TextAlign.Center
                ),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Text(
                text = if (uiState.currentUser.currentProject.isBlank()) {
                    buildAnnotatedString { append(stringResource(id = R.string.not_working_on_project)) }
                } else {
                    buildAnnotatedString {
                        append(stringResource(id = R.string.working_on))
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                color = BrandColor
                            )
                        ) {
                            append(uiState.currentUser.currentProject)
                        }
                    }
                },
                modifier = Modifier.padding(all = 8.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor,
                    textAlign = TextAlign.Center
                ),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (uiState.currentUser.userType != "Admin") {
            ProfileProgress(
                icon = painterResource(id = R.drawable.xp_icon),
                tint = Yellow,
                progress = uiState.currentUser.xp,
                progressType = ProgressType.XP
            )
            ProfileProgress(
                icon = painterResource(id = R.drawable.absent),
                tint = Green,
                progress = uiState.currentUser.attendance,
                progressType = ProgressType.ATTENDANCE
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        ProfileActionButton(
            text = stringResource(id = R.string.edit_profile),
            leadingIcon = painterResource(id = R.drawable.edit),
            onClick = onEditProfileClick
        )
        ProfileActionButton(
            text = stringResource(id = R.string.change_password),
            leadingIcon = painterResource(id = R.drawable.password),
            onClick = onChangePasswordClick
        )
        ProfileActionButton(
            text = stringResource(id = R.string.about),
            leadingIcon = painterResource(id = R.drawable.about),
            onClick = onAboutClick
        )
        ProfileActionButton(
            text = stringResource(id = R.string.logout),
            leadingIcon = painterResource(id = R.drawable.logout),
            enableTrailingIcon = false,
            onClick = {
                onLogoutClick(
                    context.getString(R.string.logout),
                    context.getString(R.string.confirm_logout)
                )
            }
        )
    }
}

@Composable
private fun LinkSection(
    uiState: UiState,
    modifier: Modifier = Modifier,
    onLinkedInClick: () -> Unit,
    onGitHubClick: () -> Unit,
    onPortfolioClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CMIconButton(
            link = uiState.currentUser.linkedInLink,
            icon = painterResource(id = R.drawable.linkedin),
            onClick = onLinkedInClick
        )
        CMIconButton(
            link = uiState.currentUser.gitHubLink,
            icon = painterResource(id = R.drawable.github),
            onClick = onGitHubClick
        )
        CMIconButton(
            link = uiState.currentUser.portfolioLink,
            icon = painterResource(id = R.drawable.more_link),
            onClick = onPortfolioClick
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        uiState = UiState(
            currentUser = User(
                username = "kawaki_22",
                name = "Prasidh Gopal Anchan",
                bio = "Android Developer | Kotlin | Compose",
                currentProject = "Project K",
                linkedInLink = "https://www.linkedin.com/in/prasidhgopal/",
                gitHubLink = "https://github.com/prasidhgopal",
                portfolioLink = "https://github.com/prasidhgopal",
                xp = 20,
                attendance = 75,
                userType = "Admin",
                mentor = "Team Android"
            )
        ),
        onEditProfileClick = { },
        onChangePasswordClick = { },
        onAboutClick = { },
        onLogoutClick = { _, _ -> }
    )
}