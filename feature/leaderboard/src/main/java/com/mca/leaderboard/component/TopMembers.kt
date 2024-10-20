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

package com.mca.leaderboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mca.ui.R
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.Green
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.util.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun TopMembers(
    topMembers: List<User>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        val top1 = topMembers[0]
        val top2 = topMembers[1]
        val top3 = topMembers[2]

        // Interchanging 0 and 1 to position the 1 member in the middle
        val newList = listOf(top2, top1, top3)

        newList
            .take(3)
            .forEachIndexed { index, user ->
                // New position for each member
                val position = when (index) {
                    0 -> 2
                    1 -> 1
                    else -> 3
                }
                CMHistogram(
                    position = position,
                    color = when (index) {
                        0 -> Yellow
                        1 -> BrandColor
                        else -> Green
                    },
                    profileImage = user.profileImage
                )
            }
    }
}

@Composable
private fun CMHistogram(
    position: Int,
    color: Color,
    profileImage: String,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    var delay by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = Unit) {
        launch {
            delay(1250L)
            visible = true
            delay = when (position) {
                1 -> 600
                2 -> 800
                else -> 1000
            }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(
                durationMillis = 600,
                delayMillis = delay
            ),
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 600),
            targetOffsetY = { it }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxHeight(
                    when (position) {
                        1 -> 1f
                        2 -> 0.8f
                        else -> 0.6f
                    }
                )
                .width(70.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = LightBlack,
                content = {
                    AsyncImage(
                        model = profileImage.ifEmpty { R.drawable.user },
                        contentDescription = position.toString(),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                modifier = Modifier
                    .height(
                        when (position) {
                            1 -> 240.dp
                            2 -> 180.dp
                            else -> 120.dp
                        }
                    )
                    .fillMaxWidth()
                    .then(modifier),
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                color = color
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = position.toString(),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosis,
                            color = fontColor
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CMHistogramPreview() {
    CMHistogram(
        position = 1,
        color = LinkBlue,
        profileImage = ""
    )
}

@Preview
@Composable
private fun TopMembersPreview() {
    TopMembers(
        topMembers = listOf(
            User(),
            User(),
            User()
        )
    )
}