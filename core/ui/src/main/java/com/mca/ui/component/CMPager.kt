/*
 * Copyright © 2025 Prasidh Gopal Anchan
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
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.mca.ui.R
import com.mca.ui.theme.Black
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.tintColor
import com.mca.util.constant.animateAlpha

/**
 * Horizontal pager composable to display post images along with the dots indicator.
 * Includes additional features such as pinch to zoom and pan.
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
    enableTransform: Boolean = true,
    onClick: () -> Unit = { },
    onRemoveImageClick: (image: String) -> Unit = { },
    onTransform: (Boolean) -> Unit = { }
) {
    val interactionSource = remember(::MutableInteractionSource)
    var isDragged by remember { mutableStateOf(false) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val transformable = rememberTransformableState { zoomChange, panChange, _ ->
        if (enableTransform) {
            scale = (scale * zoomChange).coerceAtLeast(1f)
            if (scale != 1f) offset = (offset + panChange)
        }
    }

    LaunchedEffect(key1 = transformable.isTransformInProgress) {
        if (!transformable.isTransformInProgress) {
            scale = 1f
            offset = Offset.Zero
        }
        if (transformable.isTransformInProgress) onTransform(true) else onTransform(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .clickable(
                    enabled = enableClick,
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = onClick
                )
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = true)
                        awaitDragOrCancellation(down.id)
                        isDragged = (currentEvent.changes.size == 2)
                    }
                }
                .transformable(
                    state = transformable,
                    enabled = isDragged
                )
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(images[page])
                            .scale(Scale.FIT)
                            .size(Size.ORIGINAL)
                            .build(),
                        contentDescription = stringResource(id = R.string.post_image),
                        modifier = Modifier
                            .padding(horizontal = 0.5.dp)
                            .aspectRatio(1412f / 949f),
                        contentScale = contentScale,
                        filterQuality = FilterQuality.High
                    )
                    if (enableTint) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1412f / 949f)
                                .background(color = Black.copy(alpha = 0.2f))
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
            selectedPage = state.currentPage,
            modifier = Modifier.animateAlpha(
                delay = 0,
                duration = 250,
                condition = if (enableTransform) !transformable.isTransformInProgress else true
            )
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
        state = rememberPagerState { 2 },
        enableRemoveIcon = true,
        enableTint = true
    )
}