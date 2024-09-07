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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mca.profile.UiState
import com.mca.ui.R
import com.mca.ui.component.CMButton
import com.mca.ui.component.CMIconButton
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.CMTextBox
import com.mca.ui.theme.Black
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.Red
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor
import com.mca.util.constant.LinkType
import com.mca.util.model.User

@Composable
internal fun EditProfileScreen(
    uiState: UiState,
    onUsernameChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onProfileImageChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onCurrentProjectChange: (String) -> Unit,
    onMentorChange: (String) -> Unit,
    onAddLinkCLick: (String) -> Unit,
    onRemoveLinkCLick: (LinkType) -> Unit,
    onRemoveProfileImage: () -> Unit,
    onUpdateClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) onProfileImageChange(uri.toString())
        }
    )

    var linkState by remember { mutableStateOf("") }

    val state = rememberScrollState()
    val localKeyboard = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(state),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.edit_profile),
                onBackClick = onBackClick
            )
            Box(
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(140.dp)
                        .clickable(
                            onClick = {
                                activityLauncher.launch(
                                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        ),
                    shape = CircleShape,
                    color = LightBlack,
                    content = {
                        AsyncImage(
                            model = uiState.currentUser.profileImage.ifEmpty { uiState.newProfileImage },
                            contentDescription = stringResource(id = R.string.profile),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Black.copy(0.5f))
                        )
                    }
                )
                Icon(
                    painter = painterResource(id = R.drawable.gallery),
                    contentDescription = stringResource(id = R.string.edit_profile),
                    tint = tintColor
                )
            }

            if (uiState.currentUser.profileImage.isNotEmpty()) {
                CMButton(
                    text = stringResource(id = R.string.remove_profile),
                    modifier = Modifier
                        .width(120.dp)
                        .height(35.dp),
                    fonSize = 14,
                    color = Red,
                    onClick = onRemoveProfileImage
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            CMTextBox(
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.currentUser.username,
                onValueChange = onUsernameChange,
                placeHolder = stringResource(id = R.string.username_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.username),
                        contentDescription = stringResource(id = R.string.username),
                        tint = tintColor
                    )
                },
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None
            )
            CMTextBox(
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.currentUser.name,
                onValueChange = onNameChange,
                placeHolder = stringResource(id = R.string.name_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = stringResource(id = R.string.name_placeholder),
                        tint = tintColor
                    )
                },
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            )
            CMTextBox(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(100.dp),
                value = uiState.currentUser.bio,
                onValueChange = onBioChange,
                placeHolder = stringResource(id = R.string.bio_placeholder),
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bio),
                            contentDescription = stringResource(id = R.string.bio_placeholder),
                            tint = tintColor
                        )
                    }
                },
                singleLine = false,
                maxLines = 4,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            )
            CMTextBox(
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.currentUser.currentProject,
                onValueChange = onCurrentProjectChange,
                placeHolder = stringResource(id = R.string.current_project_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.working_on),
                        contentDescription = stringResource(id = R.string.current_project_placeholder),
                        tint = tintColor
                    )
                },
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
            if (uiState.currentUser.userType != "Admin") {
                CMTextBox(
                    modifier = Modifier.padding(vertical = 8.dp),
                    value = uiState.currentUser.mentor,
                    onValueChange = onMentorChange,
                    placeHolder = stringResource(id = R.string.mentor),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.mentor),
                            contentDescription = stringResource(id = R.string.mentor),
                            tint = tintColor
                        )
                    },
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.None
                )
            }

            CMTextBox(
                modifier = Modifier.padding(vertical = 8.dp),
                value = linkState,
                onValueChange = { linkState = it },
                placeHolder = stringResource(id = R.string.add_a_link_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.more_link),
                        contentDescription = stringResource(id = R.string.add_a_link_placeholder),
                        tint = tintColor
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAddLinkCLick(linkState)
                            linkState = ""
                        },
                        enabled = uiState.currentUser.linkedInLink.isBlank() ||
                                uiState.currentUser.gitHubLink.isBlank() ||
                                uiState.currentUser.portfolioLink.isBlank(),
                        colors = IconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = tintColor,
                            disabledContentColor = tintColor.copy(alpha = 0.5f),
                            disabledContainerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = stringResource(id = R.string.add_a_link_placeholder)
                        )
                    }
                },
                keyboardType = KeyboardType.Uri,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onUpdateClick()
                        localKeyboard?.hide()
                    }
                )
            )
            AddedLinksSection(
                uiState = uiState,
                onRemoveLinkCLick = onRemoveLinkCLick
            )
            Spacer(modifier = Modifier.height(10.dp))
            CMButton(
                text = stringResource(id = R.string.update),
                enabled = !uiState.loading,
                loading = uiState.loading,
                onClick = onUpdateClick
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }

    BackHandler(onBack = onBackClick)
}

@Composable
private fun AddedLinksSection(
    uiState: UiState,
    modifier: Modifier = Modifier,
    onRemoveLinkCLick: (LinkType) -> Unit
) {
    if (uiState.currentUser.linkedInLink.isNotBlank() ||
        uiState.currentUser.gitHubLink.isNotBlank() ||
        uiState.currentUser.portfolioLink.isNotBlank()
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (uiState.currentUser.linkedInLink.isNotBlank()) {
                    CMIconButton(
                        link = uiState.currentUser.linkedInLink,
                        icon = painterResource(id = R.drawable.linkedin),
                        modifier = Modifier.padding(horizontal = 10.dp),
                        onClick = { onRemoveLinkCLick(LinkType.LINKEDIN) }
                    )
                }
                if (uiState.currentUser.gitHubLink.isNotBlank()) {
                    CMIconButton(
                        link = uiState.currentUser.gitHubLink,
                        icon = painterResource(id = R.drawable.github),
                        modifier = Modifier.padding(horizontal = 10.dp),
                        onClick = { onRemoveLinkCLick(LinkType.GITHUB) }
                    )
                }
                if (uiState.currentUser.portfolioLink.isNotBlank()) {
                    CMIconButton(
                        link = uiState.currentUser.portfolioLink,
                        icon = painterResource(id = R.drawable.more_link),
                        modifier = Modifier.padding(horizontal = 10.dp),
                        onClick = { onRemoveLinkCLick(LinkType.MORE) }
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.tap_to_remove),
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = dosis,
                    color = fontColor.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(
        uiState = UiState(
            currentUser = User(
                gitHubLink = "https://github.com/prasidhanchan/",
                portfolioLink = "https://github.com/prasidhanchan/",
                linkedInLink = "https://www.linkedin.com/in/prasidhgopal/",
            )
        ),
        onUsernameChange = { },
        onNameChange = { },
        onProfileImageChange = { },
        onBioChange = { },
        onCurrentProjectChange = { },
        onMentorChange = { },
        onAddLinkCLick = { },
        onRemoveLinkCLick = { },
        onRemoveProfileImage = { },
        onUpdateClick = { },
        onBackClick = { }
    )
}