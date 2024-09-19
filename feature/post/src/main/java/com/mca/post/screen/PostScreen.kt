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

package com.mca.post.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.post.UiState
import com.mca.ui.R
import com.mca.ui.component.CMButton
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.CMTextBox
import com.mca.ui.component.Loader
import com.mca.ui.component.SearchedTags
import com.mca.ui.theme.Black
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.Red
import com.mca.ui.theme.dosis
import com.mca.ui.theme.tintColor
import com.mca.util.constant.Constant.ADMIN
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PostScreen(
    postId: String,
    uiState: UiState,
    userType: String,
    onCurrentProjectChange: (String) -> Unit,
    onTeamMemberListChange: (List<String>) -> Unit,
    onTeamMemberChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onProgressChange: (String) -> Unit,
    onDeadlineChange: (String) -> Unit,
    onPostClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    val teamMembers = remember { mutableStateListOf<String>() }
    var newMember by remember { mutableStateOf("") }

    val state = rememberScrollState()

    LaunchedEffect(key1 = uiState.teamMembers) {
        launch {
            if (postId.isBlank() && teamMembers.isEmpty()) teamMembers.add(context.getString(R.string.me)) // If create new post add @me
            if (teamMembers.isEmpty()) {
                uiState.teamMembers.forEach { member ->
                    teamMembers.add(member)
                }
            }
        }
    }

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .imePadding()
                .fillMaxSize()
                .verticalScroll(state),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = if (postId.isBlank()) R.string.add_post else R.string.edit_post),
                onBackClick = onBackClick
            )
            CMTextBox(
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.currentProject,
                onValueChange = onCurrentProjectChange,
                placeHolder = stringResource(id = R.string.current_project_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.working_on),
                        contentDescription = stringResource(id = R.string.current_project_placeholder),
                        tint = tintColor
                    )
                },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                )
            )
            CMTextBox(
                value = newMember,
                onValueChange = {
                    newMember = it
                    onTeamMemberChange(it)
                },
                placeHolder = stringResource(id = R.string.team_member),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.team),
                        contentDescription = stringResource(id = R.string.team_member),
                        tint = tintColor
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = stringResource(id = R.string.add_member),
                        modifier = Modifier.clickable(
                            indication = null,
                            onClick = {
                                if (newMember.isNotBlank() && !teamMembers.contains(newMember)) {
                                    teamMembers.add(newMember.trim())
                                    onTeamMemberListChange(teamMembers.toList())
                                }
                                newMember = ""
                            },
                            interactionSource = interactionSource
                        ),
                        tint = tintColor
                    )
                },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                ),
                capitalization = KeyboardCapitalization.None
            )
            SearchedTags(
                tags = uiState.tags,
                onClick = { username ->
                    if (!teamMembers.any { member -> member == username }) { // Avoid duplicates
                        teamMembers.add(username)
                        onTeamMemberListChange(teamMembers.toList())
                    }
                    newMember = "" // Clear TextBox
                    onTeamMemberChange("") // Clear tags row
                }
            )
            FlowRow(
                modifier = Modifier
                    .padding(bottom = 15.dp, start = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Start
            ) {
                teamMembers.forEach { member ->
                    TeamMemberChip(
                        member = member,
                        onRemove = { oldMember ->
                            teamMembers.remove(oldMember)
                            onTeamMemberListChange(teamMembers.toList())
                        }
                    )
                }
            }
            CMTextBox(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                placeHolder = stringResource(id = R.string.short_description),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.title),
                        contentDescription = stringResource(id = R.string.description),
                        tint = tintColor
                    )
                },
                imeAction = if (userType == ADMIN) ImeAction.Next else ImeAction.Done,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (userType == ADMIN) {
                            focusManager.moveFocus(FocusDirection.Next)
                        } else {
                            focusManager.clearFocus()
                            onPostClick()
                        }
                    }
                ),
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                capitalization = KeyboardCapitalization.None,
                headerTitle = stringResource(id = R.string.description)
            )
            CMTextBox(
                value = uiState.projectProgress,
                onValueChange = onProgressChange,
                placeHolder = stringResource(id = R.string.progress_progress),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.progress),
                        contentDescription = stringResource(id = R.string.progress),
                        tint = if (userType == ADMIN) tintColor else tintColor.copy(alpha = 0.5f)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.percent),
                        contentDescription = stringResource(id = R.string.percent),
                        tint = if (userType == "Admin") tintColor else tintColor.copy(alpha = 0.5f)
                    )
                },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number,
                enabled = userType == ADMIN
            )
            CMTextBox(
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.deadline,
                onValueChange = onDeadlineChange,
                placeHolder = stringResource(id = R.string.deadline_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.deadline),
                        contentDescription = stringResource(id = R.string.deadline_placeholder),
                        tint = if (userType == ADMIN) tintColor else tintColor.copy(alpha = 0.5f)
                    )
                },
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onPostClick()
                    }
                ),
                capitalization = KeyboardCapitalization.Sentences,
                enabled = userType == ADMIN
            )
            CMButton(
                text = stringResource(id = if (postId.isBlank()) R.string.post else R.string.update),
                modifier = Modifier.padding(vertical = 20.dp),
                loading = uiState.updating,
                onClick = {
                    if (uiState.currentProject.isNotBlank() && uiState.teamMembers.size > 1) {
                        focusManager.clearFocus()
                    }
                    onPostClick()
                }
            )
        }
    }

    Loader(loading = uiState.loading)
}

@Composable
private fun TeamMemberChip(
    member: String,
    modifier: Modifier = Modifier,
    onRemove: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp, start = 6.dp, end = 10.dp)
            .height(35.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .then(modifier),
        shape = RoundedCornerShape(8.dp),
        color = LightBlack
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(id = R.string.username, member),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = LinkBlue
                ),
                modifier = Modifier.padding(bottom = 4.dp, end = 5.dp)
            )
            if (member != stringResource(id = R.string.me)) {
                Icon(
                    painter = painterResource(id = R.drawable.remove),
                    contentDescription = stringResource(id = R.string.remove_member),
                    modifier = Modifier.clickable(
                        indication = null,
                        onClick = { onRemove(member) },
                        interactionSource = interactionSource
                    ),
                    tint = Red
                )
            }
        }
    }
}

@Preview
@Composable
private fun PostScreenPreview() {
    PostScreen(
        postId = "",
        uiState = UiState(),
        userType = "student",
        onCurrentProjectChange = { },
        onTeamMemberListChange = { },
        onTeamMemberChange = { },
        onDescriptionChange = { },
        onProgressChange = { },
        onDeadlineChange = { },
        onPostClick = { },
        onBackClick = { }
    )
}