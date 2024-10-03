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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mca.post.UiState
import com.mca.ui.R
import com.mca.ui.component.CMButton
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
        contract = ActivityResultContracts.PickMultipleVisualMedia(4)
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMRegularAppBar(
                text = stringResource(id = R.string.add_announcement),
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
                placeHolder = stringResource(id = R.string.whats_going_on),
                imeAction = ImeAction.Default,
                capitalization = KeyboardCapitalization.Sentences,
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                enableHeader = false
            )
            Text(
                text = if (uiState.description.length >= 300)
                    "Max characters reached"
                else "${300 - uiState.description.length} characters",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = dosis,
                    color = if (uiState.description.length >= 300) Red else fontColor.copy(0.5f)
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            CMButton(
                text = stringResource(id = R.string.post),
                modifier = Modifier.fillMaxWidth(0.9f),
                loading = uiState.loading,
                enabled = !uiState.loading,
                color = Color.White,
                textColor = Black,
                onClick = { onPostClick(images) }
            )
            Box(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "Once you make an announcement it cannot be edited later. \nMake sure it's accurate!",
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
        Surface(
            modifier = modifier
                .padding(all = 20.dp)
                .fillMaxWidth()
                .height(250.dp)
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
            if (images.isEmpty()) {
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
                        text = stringResource(id = R.string.select_your_images),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = dosis,
                            color = fontColor.copy(0.5f)
                        )
                    )
                }
            } else {
                HorizontalPager(state = state) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            AsyncImage(
                                model = images[page],
                                contentDescription = stringResource(id = R.string.post_image),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = Black.copy(0.2f))
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = stringResource(id = R.string.remove_image),
                                modifier = Modifier
                                    .padding(all = 10.dp)
                                    .clickable(
                                        onClick = { onRemoveImageClick(images[page]) }
                                    ),
                                tint = tintColor
                            )
                        }
                    }
                }
            }
        }

        PagerDot(
            pageCount = state.pageCount,
            selectedPage = state.currentPage
        )
    }
}

@Composable
private fun PagerDot(
    pageCount: Int,
    selectedPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (page in 0 until pageCount) {
            Box(
                modifier = modifier
                    .padding(horizontal = 5.dp)
                    .clip(CircleShape)
                    .background(color = if (page == selectedPage) Color.White else Color.Gray)
                    .size(5.dp)
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