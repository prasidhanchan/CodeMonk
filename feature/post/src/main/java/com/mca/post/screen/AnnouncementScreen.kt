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

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.post.UiState
import com.mca.ui.R
import com.mca.ui.component.CMButton
import com.mca.ui.component.CMPager
import com.mca.ui.component.CMRegularAppBar
import com.mca.ui.component.CMTextBox
import com.mca.ui.theme.Black
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.Red
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor

@Composable
internal fun AnnouncementScreen(
    uiState: UiState,
    onDescriptionChange: (String) -> Unit,
    onPostClick: (images: List<String>) -> Unit,
    onBackClick: () -> Unit
) {
    val images = remember { mutableStateListOf<String>() }
    val activityResultsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            (4 - images.size).coerceAtLeast(2)
        )
    ) { result ->
        result.forEach { uri -> images.add(uri.toString()) }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .imePadding()
                .padding(all = 15.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.new_announcement),
                onBackClick = onBackClick
            )
            ImagePicker(
                images = images,
                activityResultLauncher = activityResultsLauncher,
                onRemoveImageClick = { image -> images.remove(image) }
            )
            CMTextBox(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                placeHolder = stringResource(id = R.string.whats_going_on),
                imeAction = ImeAction.Default,
                capitalization = KeyboardCapitalization.Sentences,
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                enableHeader = false
            )
            Text(
                text = stringResource(
                    id = R.string.character_count,
                    uiState.description.length
                ),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = dosis,
                    color = if (uiState.description.length > 300) Red else fontColor.copy(0.5f)
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            CMButton(
                text = stringResource(id = R.string.post),
                modifier = Modifier.fillMaxWidth(),
                loading = uiState.loading,
                enabled = !uiState.loading,
                color = Color.White,
                textColor = Black,
                onClick = { onPostClick(images) }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = stringResource(id = R.string.announcement_note),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = dosis,
                        color = fontColor
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ImagePicker(
    images: List<String>,
    activityResultLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    onRemoveImageClick: (image: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberPagerState { images.size }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (images.isEmpty()) {
            Surface(
                modifier = modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth()
                    .height(260.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember(::MutableInteractionSource),
                        enabled = images.size < 4,
                        onClickLabel = stringResource(id = R.string.select_your_images),
                        onClick = {
                            activityResultLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ),
                shape = RoundedCornerShape(10.dp),
                color = LightBlack
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = stringResource(id = R.string.add_image),
                        tint = tintColor.copy(0.5f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(id = R.string.select_your_images))
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            ) {
                                append(stringResource(id = R.string.recommended_size))
                            }
                        },
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = dosis,
                            color = fontColor.copy(0.5f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            CMPager(
                images = images,
                state = state,
                enableTint = true,
                enableRemoveIcon = true,
                enableClick = images.size < 4,
                enableTransform = false,
                onClick = {
                    activityResultLauncher.launch(
                        PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onRemoveImageClick = onRemoveImageClick
            )
        }
    }
}

@Preview
@Composable
private fun AnnouncementScreenPreview() {
    AnnouncementScreen(
        uiState = UiState(
            description = "Introducing CodeLab, \n\n" +
                    "the ultimate app for developers to manage coding projects with ease. " +
                    "Whether working solo or in teams, CodeLab offers real-time updates, progress tracking, and a sleek interface. Stay organized, collaborate effortlessly, and take your coding projects to the next level!"
        ),
        onDescriptionChange = { },
        onPostClick = { },
        onBackClick = { }
    )
}