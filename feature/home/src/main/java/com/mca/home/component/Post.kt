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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.ads.nativead.NativeAd
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
import com.mca.ui.theme.tintColor
import com.mca.util.constant.Constant.ADMIN
import com.mca.util.constant.Constant.MAX_POST_ADS
import com.mca.util.constant.animatedLike
import com.mca.util.constant.toLikedBy
import com.mca.util.constant.toLikes
import com.mca.util.constant.toPostId
import com.mca.util.constant.toTimeStamp
import com.mca.util.model.Post
import com.mca.util.model.User

/**
 * Post composable to display user Post.
 */
@Composable
internal fun Post(
    posts: () -> List<Pair<Post, User>>,
    currentUserId: String,
    currentUsername: String,
    currentUserType: String,
    loading: Boolean,
    modifier: Modifier = Modifier,
    state: LazyListState,
    onLikeClick: (postId: String, token: String) -> Unit,
    onUnlikeClick: (postId: String) -> Unit,
    onUsernameClick: (String) -> Unit,
    onEditPostClick: (postId: String) -> Unit,
    onDeleteClick: (postId: String) -> Unit,
    nativeAds: List<NativeAd?>,
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

        if (posts().isNotEmpty() && !loading) {
            itemsIndexed(items = posts()) { index, (post, user) ->
                PostCard(
                    post = post,
                    user = user,
                    currentUserId = currentUserId,
                    currentUsername = currentUsername,
                    currentUserType = currentUserType,
                    onLikeClick = onLikeClick,
                    onUnlikeClick = onUnlikeClick,
                    onUsernameClick = onUsernameClick,
                    onEditPostClick = onEditPostClick,
                    onDeleteClick = onDeleteClick
                )
                if (index < MAX_POST_ADS && nativeAds.size == MAX_POST_ADS) {
                    PostNativeAd(nativeAd = nativeAds[index])
                }
            }
        } else if (posts().isEmpty() && loading) {
            items(count = 2) {
                PostCardLoader()
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun PostCard(
    post: Post,
    user: User,
    currentUserId: String,
    currentUsername: String,
    currentUserType: String,
    modifier: Modifier = Modifier,
    onLikeClick: (postId: String, token: String) -> Unit,
    onUnlikeClick: (postId: String) -> Unit,
    onUsernameClick: (String) -> Unit,
    onEditPostClick: (postId: String) -> Unit,
    onDeleteClick: (postId: String) -> Unit
) {
    Column(
        modifier
            .padding(vertical = 15.dp)
            .animateContentSize(animationSpec = tween(durationMillis = 400))
            .wrapContentHeight(Alignment.CenterVertically)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        PostTopBar(
            post = post,
            user = user,
            currentUserId = currentUserId,
            onUsernameClick = onUsernameClick,
            onDeleteClick = onDeleteClick
        )
        MainContent(
            post = post,
            user = user,
            mentor = user.mentor,
            currentUserId = currentUserId,
            currentUsername = currentUsername,
            currentUserType = currentUserType,
            onUsernameClick = onUsernameClick,
            onEditPostClick = onEditPostClick,
            onLikeClick = onLikeClick,
            onUnlikeClick = onUnlikeClick
        )
        PostDescription(
            description = post.description,
            username = user.username
        )
        if (post.likes.isNotEmpty()) PostBottomBar(post = post)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MainContent(
    post: Post,
    user: User,
    mentor: String,
    currentUserId: String,
    currentUsername: String,
    currentUserType: String,
    modifier: Modifier = Modifier,
    onUsernameClick: (String) -> Unit,
    onEditPostClick: (postId: String) -> Unit,
    onLikeClick: (postId: String, token: String) -> Unit,
    onUnlikeClick: (postId: String) -> Unit
) {
    var isLiked by remember(post.likes) { mutableStateOf(post.likes.contains(currentUsername)) }
    var likes by remember(post.likes) { mutableIntStateOf(post.likes.size) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        shape = RoundedCornerShape(15.dp),
        color = LightBlack
    ) {
        Column(
            modifier = modifier
                .padding(all = 15.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                if (post.userId == currentUserId || currentUserType == ADMIN) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit_post),
                        contentDescription = stringResource(id = R.string.edit_post),
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { onEditPostClick(post.toPostId()) }
                        ),
                        tint = tintColor
                    )
                }
            }
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
                    UsernameChip(
                        username = member,
                        fontColor = LinkBlue,
                        onUsernameClick = onUsernameClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            MyMentor(
                mentor = mentor,
                onUsernameClick = onUsernameClick
            )
            ProjectProgress(post = post)
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
                        if (post.deadline.isBlank()) {
                            append(stringResource(R.string.not_set))
                        } else {
                            append(post.deadline)
                        }
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
                        .animatedLike(
                            onClick = {
                                if (isLiked) {
                                    isLiked = false
                                    likes -= 1
                                    onUnlikeClick(post.toPostId())
                                } else {
                                    isLiked = true
                                    likes += 1
                                    onLikeClick(post.toPostId(), user.token)
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
    user: User,
    currentUserId: String,
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
                onClick = { onUsernameClick(user.username) }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = user.profileImage.ifEmpty { R.drawable.user },
            contentDescription = user.username,
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(CircleShape)
                .background(LightBlack)
                .size(30.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = user.username,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor
            ),
            modifier = Modifier.padding(end = 5.dp)
        )
        if (user.isVerified || user.userType == ADMIN) {
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
                        onClick = { onDeleteClick(post.toPostId()) }
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
            .padding(vertical = 8.dp)
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
private fun UsernameChip(
    username: String,
    modifier: Modifier = Modifier,
    isVerified: Boolean = false,
    fontColor: Color,
    onUsernameClick: (String) -> Unit
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .height(35.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    if (username != context.getString(R.string.me)) onUsernameClick(username)
                }
            ),
        shape = RoundedCornerShape(8.dp),
        color = ExtraLightBlack
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxHeight()
                .wrapContentWidth(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
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
                modifier = Modifier.padding(bottom = 3.dp)
            )
            if (isVerified) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.tick),
                    contentDescription = stringResource(id = R.string.blue_tick),
                    tint = LinkBlue
                )
            }
        }
    }
}

@Composable
private fun MyMentor(
    mentor: String,
    modifier: Modifier = Modifier,
    onUsernameClick: (String) -> Unit
) {
    if (mentor.isNotBlank()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.mentor),
                modifier = modifier,
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
            Spacer(modifier = Modifier.height(10.dp))
            UsernameChip(
                username = mentor,
                isVerified = true,
                fontColor = LinkBlue,
                onUsernameClick = onUsernameClick
            )
        }
    }
}

@Composable
private fun ProjectProgress(
    post: Post,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.progress_header),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor
            )
        )
        CMProgressBar(progress = (post.projectProgress / 100f))
        Text(
            text = stringResource(
                R.string.progress_percentage,
                post.projectProgress
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
    }
}

@Composable
private fun PostCardLoader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(vertical = 10.dp)
            .height(465.dp)
            .fillMaxWidth(),
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
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .height(320.dp),
            shape = RoundedCornerShape(15.dp),
            color = LightBlack,
            content = { }
        )
        Surface(
            modifier = modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(0.9f)
                .height(20.dp),
            shape = CircleShape,
            color = LightBlack,
            content = { }
        )
        Surface(
            modifier = modifier
                .padding(vertical = 10.dp)
                .width(250.dp)
                .height(15.dp),
            shape = CircleShape,
            color = LightBlack,
            content = { }
        )
    }
}

@Composable
private fun PostDescription(
    description: String,
    username: String,
    modifier: Modifier = Modifier
) {
    if (description.isNotBlank()) {
        var isOpen by remember { mutableStateOf(false) }

        Row(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 5.dp)
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember(::MutableInteractionSource),
                    onClick = { isOpen = !isOpen }
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(fontWeight = FontWeight.ExtraBold)
                    ) {
                        append(username)
                    }
                    append("  ")
                    append(description)
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = dosis,
                    color = fontColor
                ),
                maxLines = if (!isOpen) 2 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis
            )
        }
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
            currentProject = "Project X",
            teamMembers = listOf(
                "me",
                "naruto",
                "uchiha_sasuke",
                "kakashi"
            ),
            description = "TaskBuddy helps you stay organized and productive by managing your to-do lists, setting reminders.",
            projectProgress = 20,
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
        user = User(
            username = "pra_sidh_22"
        ),
        currentUserId = "1",
        currentUsername = "pra_sidh_22",
        currentUserType = "student",
        onLikeClick = { _, _ -> },
        onUnlikeClick = { },
        onUsernameClick = { },
        onEditPostClick = { },
        onDeleteClick = { }
    )
}

@Preview
@Composable
private fun MemberCardPreview() {
    UsernameChip(
        username = "kawaki",
        fontColor = Blue,
        onUsernameClick = { }
    )
}