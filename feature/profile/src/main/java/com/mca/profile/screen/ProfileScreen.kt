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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import com.mca.profile.component.LinkSection
import com.mca.profile.component.ProfileActionButton
import com.mca.profile.component.ProfileProgress
import com.mca.profile.component.ProgressType
import com.mca.ui.R
import com.mca.ui.component.CMAlertDialog
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.Loader
import com.mca.ui.theme.Black
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.Green
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.util.constant.Constant.ADMIN
import com.mca.util.model.User

@Composable
internal fun ProfileScreen(
    uiState: UiState,
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
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
                        model = uiState.currentUser.profileImage.ifEmpty { R.drawable.user },
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
            LinkSection(
                user = uiState.currentUser,
                modifier = Modifier.padding(top = 10.dp)
            )
            MyUsernameCard(
                username = uiState.currentUser.username,
                userType = uiState.currentUser.userType,
                isVerified = uiState.currentUser.verified
            )
            if (uiState.currentUser.userType == ADMIN) {
                Text(
                    text = stringResource(R.string.mentor_for, uiState.currentUser.mentorFor),
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

            Spacer(modifier = Modifier.height(14.dp))

            if (uiState.currentUser.userType != ADMIN) {
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
                onClick = { visible = true }
            )
        }
    }

    CMAlertDialog(
        title = stringResource(id = R.string.logout),
        message = stringResource(R.string.confirm_logout),
        visible = visible,
        confirmText = stringResource(id = R.string.confirm),
        dismissText = stringResource(id = R.string.cancel),
        onConfirm = {
            visible = false
            onLogoutClick()
        },
        onDismiss = { visible = false }
    )

    Loader(loading = uiState.loading)
}

@Composable
private fun MyUsernameCard(
    username: String,
    userType: String,
    isVerified: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(bottom = 10.dp)
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
                text = stringResource(id = R.string.username_tag, username),
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
            if (isVerified || userType == ADMIN) {
                Icon(
                    painter = painterResource(id = R.drawable.tick),
                    contentDescription = stringResource(id = R.string.blue_tick),
                    modifier = Modifier.padding(bottom = 1.dp),
                    tint = LinkBlue
                )
            }
        }
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
                userType = "student",
                mentor = "pra_sidh_22",
                mentorFor = "Team Android"
            ),
            loading = false
        ),
        onEditProfileClick = { },
        onChangePasswordClick = { },
        onAboutClick = { },
        onLogoutClick = { }
    )
}