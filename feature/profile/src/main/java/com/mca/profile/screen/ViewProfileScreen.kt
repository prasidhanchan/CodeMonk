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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.mca.profile.component.ProfileProgress
import com.mca.profile.component.ProgressType
import com.mca.ui.R
import com.mca.ui.component.CMProfileCard
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.Loader
import com.mca.ui.theme.Black
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.util.constant.Constant.ADMIN
import com.mca.util.constant.animateAlpha
import com.mca.util.model.User

@Composable
internal fun ViewProfileScreen(
    uiState: UiState,
    onProfileCardClick: (username: String) -> Unit,
    onBackClick: () -> Unit
) {
    val isTopMember by remember(uiState.selectedUser) {
        mutableStateOf(uiState.topMembers.any { member -> member == uiState.selectedUser?.userId })
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        if (uiState.selectedUser != null) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CMRegularAppBar(
                    text = stringResource(
                        id = R.string.username_tag,
                        uiState.selectedUser?.username.orEmpty()
                    ),
                    onBackClick = onBackClick
                )
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = LightBlack,
                    content = {
                        AsyncImage(
                            model = uiState.selectedUser?.profileImage?.ifEmpty { R.drawable.user },
                            contentDescription = uiState.selectedUser?.username,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (uiState.selectedUser?.verified!! ||
                        uiState.selectedUser?.userType == ADMIN ||
                        isTopMember
                    ) {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = uiState.selectedUser?.name!!,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = dosis,
                            color = fontColor
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    if (uiState.selectedUser?.verified!! || uiState.selectedUser?.userType == ADMIN) {
                        Icon(
                            painter = painterResource(id = R.drawable.tick),
                            contentDescription = stringResource(id = R.string.blue_tick),
                            modifier = Modifier.padding(top = 2.dp, start = 5.dp),
                            tint = LinkBlue
                        )
                    } else if (isTopMember) {
                        Icon(
                            painter = painterResource(id = R.drawable.crown),
                            contentDescription = stringResource(id = R.string.crown),
                            modifier = Modifier
                                .padding(top = 2.dp, start = 5.dp)
                                .size(18.dp),
                            tint = Yellow
                        )
                    }
                }

                Text(
                    text = uiState.selectedUser?.bio!!,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = fontColor.copy(alpha = 0.8f)
                    ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                LinkSection(user = uiState.selectedUser!!)
                if (uiState.selectedUser?.userType != ADMIN) {
                    Text(
                        text = if (uiState.selectedUser?.currentProject?.isNotBlank()!!) {
                            buildAnnotatedString {
                                append(stringResource(id = R.string.currently_working_on))
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = LinkBlue
                                    )
                                ) {
                                    append(uiState.selectedUser?.currentProject)
                                }
                            }
                        } else {
                            buildAnnotatedString {
                                append(stringResource(id = R.string.not_working_on_project))
                            }
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = fontColor
                    )
                }
                if (uiState.selectedUser?.mentorFor?.isNotBlank()!!) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(
                            id = R.string.mentor_for,
                            uiState.selectedUser?.mentorFor!!
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = LinkBlue
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                if (uiState.selectedUser?.userType != ADMIN) {
                    ProfileProgress(
                        progress = uiState.selectedUser?.xp!!,
                        progressType = ProgressType.XP,
                        header = stringResource(id = R.string.dev_experience),
                        icon = painterResource(id = R.drawable.xp_icon),
                        color = Yellow
                    )
                    ProfileProgress(
                        progress = uiState.selectedUser?.currentProjectProgress!!,
                        progressType = ProgressType.PROJECT_PROGRESS,
                        header = stringResource(id = R.string.current_project_progress),
                        icon = painterResource(id = R.drawable.working_on),
                        color = BrandColor
                    )
                } else {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(0.8f))
                    OtherMentorsSection(
                        mentors = uiState.otherMentors,
                        onProfileCardClick = onProfileCardClick
                    )
                }
            }
        }
    }

    BackHandler(onBack = onBackClick)
    Loader(loading = uiState.selectedUser == null)
}

@Composable
private fun OtherMentorsSection(
    mentors: List<User>,
    modifier: Modifier = Modifier,
    onProfileCardClick: (userId: String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(vertical = 40.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.other_mentors),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor
            )
        )
        Column(
            modifier = modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mentors.forEachIndexed { index, mentor ->
                CMProfileCard(
                    user = mentor,
                    modifier = Modifier.animateAlpha(index * 100),
                    onClick = onProfileCardClick
                )
            }
        }
    }
}

@Preview
@Composable
private fun ViewProfileScreenPreview() {
    ViewProfileScreen(
        uiState = UiState(
            selectedUser = User(
                username = "pra_sidh_22",
                name = "Prasidh Gopal Anchan",
                bio = "Android Developer | Kotlin | Compose",
                linkedInLink = "https://www.linkedin.com/in/pra_sidh_22/",
                gitHubLink = "https://github.com/pra_sidh_22",
                portfolioLink = "https://codemonk.club",
                currentProject = "Project K",
                currentProjectProgress = 50,
                xp = 20,
                userType = "student",
                mentor = "pra_sidh_22",
                mentorFor = ""
            ),
            otherMentors = listOf(
                User(
                    username = "shahiz",
                    name = "Shahiz Moidin",
                    bio = "Flutter Developer"
                ),
                User(
                    username = "demonlord",
                    name = "Sathwik Shetty",
                    bio = "Mentor for 2nd years"
                ),
                User(
                    username = "kawaki",
                    name = "Kawaki",
                    bio = "Shinobi no jidaiwa ovaru"
                )
            )
        ),
        onProfileCardClick = { },
        onBackClick = { }
    )
}