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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mca.ui.R
import com.mca.ui.theme.Black
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.tintColor

/**
 * Horizontal pager composable to display post images along with the dots indicator.
 * @param images List of image URLs to be displayed in the pager.
 * @param state PagerState to control and observe the pager's state.
 * @param modifier Modifier for styling and layout customization.
 * @param contentScale Scale to apply to the image content.
 * @param enableTint Enable or disable the tint effect on the image.
 * @param enableRemoveIcon Enable or disable the remove icon on the image.
 * @param enableClick Enable or disable the click interaction on the image.
 * @param onClick Callback to be invoked when the image is clicked.
 * @param onRemoveImageClick Callback to be invoked when the remove icon is clicked.
 */
@Composable
fun CMPager(
    images: List<String>,
    state: PagerState,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    enableTint: Boolean = false,
    enableRemoveIcon: Boolean = false,
    enableClick: Boolean = false,
    onClick: () -> Unit = { },
    onRemoveImageClick: (image: String) -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(265.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = modifier
                .padding(top = 4.dp, bottom = 10.dp)
                .fillMaxWidth()
                .height(220.dp)
                .clickable(
                    enabled = enableClick,
                    indication = null,
                    interactionSource = remember(::MutableInteractionSource),
                    onClick = onClick
                ),
            shape = RoundedCornerShape(10.dp),
            color = LightBlack
        ) {
            HorizontalPager(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically)
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AsyncImage(
                        model = images[page],
                        contentDescription = stringResource(id = R.string.post_image),
                        modifier = Modifier
                            .padding(horizontal = 0.5.dp)
                            .fillMaxSize(),
                        contentScale = contentScale
                    )
                    if (enableTint) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Black.copy(0.2f))
                        )
                    }
                    if (enableRemoveIcon) {
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
    if (pageCount > 1) {
        Row(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 20.dp)
                .fillMaxWidth(),
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
}

@Preview
@Composable
private fun CMPagerPreview() {
    CMPager(
        images = listOf("image1", "image2"),
        state = rememberPagerState { 2 }
    )
}