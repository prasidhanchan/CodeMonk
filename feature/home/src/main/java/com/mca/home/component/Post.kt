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

package com.mca.home.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mca.ui.R
import com.mca.ui.component.CMProgressBar
import com.mca.ui.theme.Blue
import com.mca.ui.theme.ExtraLightBlack
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LightRed
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.Red
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.util.constants.toLikedBy
import com.mca.util.constants.toLikes
import com.mca.util.constants.toTimeStamp
import com.mca.util.model.Post
import kotlin.math.roundToInt

/**
 * Post composable to display user Post.
 */
@Composable
fun Post(
    posts: List<Post>,
    isVerified: (String) -> Boolean,
    currentUserId: String,
    loading: Boolean,
    modifier: Modifier = Modifier,
    state: LazyListState,
    onLikeClick: () -> Unit,
    onUnlikeClick: () -> Unit,
    onUsernameClick: (String) -> Unit,
    onDeleteClick: (postId: String) -> Unit,
    appBar: @Composable () -> Unit = { }
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(key = "HomeAppbar") {
            appBar()
        }

        if (posts.isNotEmpty() && !loading) {
            items(items = posts) { post ->
                PostCard(
                    post = post,
                    isVerified = isVerified,
                    currentUserId = currentUserId,
                    onLikeClick = onLikeClick,
                    onUnlikeClick = onUnlikeClick,
                    onUsernameClick = onUsernameClick,
                    onDeleteClick = onDeleteClick
                )
            }
        } else if (posts.isEmpty() && !loading) {
            items(count = 2) {
                PostCardLoader()
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun PostCard(
    post: Post,
    isVerified: (String) -> Boolean,
    currentUserId: String,
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit,
    onUnlikeClick: () -> Unit,
    onUsernameClick: (String) -> Unit,
    onDeleteClick: (postId: String) -> Unit
) {
    Column(
        modifier
            .padding(vertical = 10.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .width(350.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        PostTopBar(
            post = post,
            isVerified = isVerified,
            currentUserId = currentUserId,
            onUsernameClick = onUsernameClick,
            onDeleteClick = onDeleteClick
        )
        MainContent(
            post = post,
            onUsernameClick = onUsernameClick,
            onLikeClick = onLikeClick,
            onUnlikeClick = onUnlikeClick
        )
        PostBottomBar(post = post)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainContent(
    post: Post,
    modifier: Modifier = Modifier,
    onUsernameClick: (String) -> Unit,
    onLikeClick: () -> Unit,
    onUnlikeClick: () -> Unit
) {
    var isLiked by remember(post.likes) { mutableStateOf(post.likes.contains(post.userId)) }
    var likes by remember(post.likes) { mutableIntStateOf(post.likes.size) }

    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .animateContentSize(animationSpec = tween(durationMillis = 800))
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        shape = RoundedCornerShape(15.dp),
        color = LightBlack
    ) {
        Column(
            modifier = modifier
                .padding(all = 15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.currently_working_on))
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            color = Yellow
                        )
                    ) {
                        append(post.currentProject)
                    }
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.team_members),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                )
            )
            FlowRow(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (member in post.teamMembers) {
                    ActionChip(
                        username = member,
                        fontColor = LinkBlue,
                        onUsernameClick = onUsernameClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.project_progress),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                )
            )
            CMProgressBar(progress = post.projectProgress)
            Text(
                text = stringResource(
                    R.string.progress_percentage,
                    (post.projectProgress * 100).roundToInt()
                ),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = dosis,
                    color = fontColor,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.deadline))
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            color = LightRed
                        )
                    ) {
                        append(post.deadline)
                    }
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = if (isLiked) R.drawable.like else R.drawable.unlike),
                    contentDescription = stringResource(id = if (isLiked) R.string.unlike else R.string.like),
                    tint = if (isLiked) Red else Color.White,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(25.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = {
                                if (isLiked) {
                                    isLiked = false
                                    likes -= 1
                                    onUnlikeClick()
                                } else {
                                    isLiked = true
                                    likes += 1
                                    onLikeClick()
                                }
                            }
                        )
                )
                Text(
                    text = likes.toLikes(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = fontColor
                    )
                )
                Text(
                    text = post.timeStamp.toTimeStamp(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = fontColor.copy(alpha = 0.5f),
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}

@Composable
private fun PostTopBar(
    post: Post,
    currentUserId: String,
    isVerified: (String) -> Boolean,
    modifier: Modifier = Modifier,
    onUsernameClick: (String) -> Unit,
    onDeleteClick: (postId: String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { onUsernameClick(post.username) }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = post.userImage,
            contentDescription = post.username,
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(CircleShape)
                .background(LightBlack)
                .size(30.dp)
        )
        Text(
            text = post.username,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor
            ),
            modifier = Modifier.padding(end = 5.dp)
        )
        if (isVerified(post.username)) {
            Icon(
                painter = painterResource(id = R.drawable.tick),
                contentDescription = stringResource(id = R.string.blue_tick),
                tint = LinkBlue
            )
        }

        if (currentUserId == post.userId) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = stringResource(id = R.string.delete),
                    tint = Red,
                    modifier = Modifier.clickable(
                        onClick = { onDeleteClick("${post.username}-${post.timeStamp}") }
                    )
                )
            }
        }
    }
}

@Composable
private fun PostBottomBar(
    post: Post,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = R.drawable.like),
            contentDescription = stringResource(id = R.string.like),
            tint = Red,
            modifier = Modifier
                .padding(end = 5.dp)
                .size(15.dp)
        )
        Text(
            text = post.likes.toLikedBy(),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = dosis,
                color = fontColor
            )
        )
    }
}

@Composable
private fun ActionChip(
    username: String,
    fontColor: Color,
    modifier: Modifier = Modifier,
    onUsernameClick: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .height(35.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onUsernameClick(username) }
            ),
        shape = RoundedCornerShape(8.dp),
        color = ExtraLightBlack
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxHeight()
                .wrapContentWidth(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.username, username),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
private fun PostCardLoader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(vertical = 10.dp)
            .height(360.dp)
            .width(350.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(30.dp),
                shape = CircleShape,
                color = LightBlack,
                content = { }
            )
            Surface(
                modifier = Modifier
                    .width(80.dp)
                    .height(15.dp),
                shape = CircleShape,
                color = LightBlack,
                content = { }
            )
        }

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(270.dp),
            shape = RoundedCornerShape(15.dp),
            color = LightBlack,
            content = { }
        )
        Surface(
            modifier = modifier
                .padding(vertical = 10.dp)
                .width(300.dp)
                .height(20.dp),
            shape = CircleShape,
            color = LightBlack,
            content = { }
        )
    }
}

@Preview
@Composable
private fun PostCardLoaderPreview() {
    PostCardLoader()
}

@Preview
@Composable
private fun PostCardPreview() {
    PostCard(
        post = Post(
            userId = "1",
            username = "kawaki_22",
            userImage = "",
            currentProject = "Project X",
            teamMembers = listOf(
                "me",
                "naruto",
                "uchiha_sasuke",
                "kakashi"
            ),
            projectProgress = 0.2f,
            deadline = "30 Nov, 2024",
            likes = listOf(
                "naruto",
                "kakashi",
                "sasuke",
                "minato",
                "itachi"
            ),
            timeStamp = 1690885200000L
        ),
        isVerified = { true },
        currentUserId = "1",
        onLikeClick = { },
        onUnlikeClick = { },
        onUsernameClick = { },
        onDeleteClick = { }
    )
}

@Preview
@Composable
private fun MemberCardPreview() {
    ActionChip(
        username = "kawaki",
        fontColor = Blue,
        onUsernameClick = { }
    )
}