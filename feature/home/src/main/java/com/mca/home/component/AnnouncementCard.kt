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

package com.mca.home.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mca.ui.R
import com.mca.ui.component.CMPager
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.Red
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor
import com.mca.util.constant.Constant.ADMIN
import com.mca.util.constant.animateAlpha
import com.mca.util.constant.animatedLike
import com.mca.util.constant.extractUrl
import com.mca.util.constant.toLikedBy
import com.mca.util.constant.toLikes
import com.mca.util.constant.toPostId
import com.mca.util.constant.toTimeStamp
import com.mca.util.model.Post
import com.mca.util.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun AnnouncementCard(
    post: Post,
    user: User,
    currentUserId: String,
    currentUsername: String,
    topMembers: List<String>,
    modifier: Modifier = Modifier,
    onUsernameClick: (username: String) -> Unit,
    onDeleteClick: (postId: String) -> Unit,
    onLikeClick: (postId: String, token: String) -> Unit,
    onUnlikeCLick: (postId: String) -> Unit
) {
    var alpha by remember { mutableFloatStateOf(1f) }

    Column(
        modifier = modifier
            .animateContentSize(animationSpec = tween(durationMillis = 400))
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        AnnouncementTopBar(
            post = post,
            user = user,
            currentUserId = currentUserId,
            topMembers = topMembers,
            onUsernameClick = onUsernameClick,
            onDeleteClick = onDeleteClick
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically),
            shape = RoundedCornerShape(15.dp),
            color = LightBlack
        ) {
            Column(
                modifier = modifier
                    .padding(all = 15.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                MainContent(
                    post = post,
                    alpha = alpha,
                    onTransform = { transforming ->
                        alpha = if (transforming) 0f else 1f
                    }
                )
                LikesAndTimeStamp(
                    post = post,
                    token = user.token,
                    currentUserId = currentUserId,
                    currentUsername = currentUsername,
                    modifier = Modifier.animateAlpha(
                        delay = 0,
                        duration = 250,
                        condition = alpha == 1f
                    ),
                    onLikeCLick = onLikeClick,
                    onUnlikeCLick = onUnlikeCLick
                )
            }
        }

        if (post.likes.isNotEmpty()) AnnouncementBottomBar(post = post)
    }
}

@Composable
private fun AnnouncementTopBar(
    post: Post,
    user: User,
    currentUserId: String,
    topMembers: List<String>,
    modifier: Modifier = Modifier,
    onUsernameClick: (username: String) -> Unit,
    onDeleteClick: (postId: String) -> Unit
) {
    Row(
        modifier = modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember(::MutableInteractionSource),
                onClick = { onUsernameClick(post.userId) }
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
                .background(color = LightBlack)
                .size(30.dp)
        )
        if (!post.userLoading) {
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
        } else {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 5.dp)
                    .fillMaxWidth(0.3f)
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(color = LightBlack)
            )
        }
        if (user.userType == ADMIN || user.verified) {
            Icon(
                painter = painterResource(id = R.drawable.tick),
                contentDescription = stringResource(id = R.string.blue_tick),
                tint = LinkBlue
            )
        } else if (topMembers.any { member -> member == user.userId }) {
            Icon(
                painter = painterResource(id = R.drawable.crown),
                contentDescription = stringResource(id = R.string.crown),
                tint = Yellow
            )
        }
        if (currentUserId == user.userId) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = stringResource(id = R.string.delete_announcement),
                    modifier = Modifier.clickable(onClick = { onDeleteClick(post.toPostId()) }),
                    tint = Red
                )
            }
        }
    }
}

@Composable
private fun AnnouncementBottomBar(
    post: Post,
    modifier: Modifier = Modifier
) {
    if (!post.likesLoading) {
        Row(
            modifier = modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.like),
                contentDescription = stringResource(id = R.string.like),
                modifier = Modifier.size(15.dp),
                tint = Red
            )
            Text(
                text = post.likes
                    .toLikedBy(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = dosis,
                    color = fontColor
                ),
                modifier = Modifier.padding(start = 5.dp),
                textAlign = TextAlign.End
            )
        }
    } else {
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(0.55f)
                .height(14.dp)
                .clip(CircleShape)
                .background(color = LightBlack)
        )
    }
}

@Composable
private fun MainContent(
    post: Post,
    modifier: Modifier = Modifier,
    alpha: Float,
    onTransform: (Boolean) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val state = rememberPagerState { post.images.size }
    var isOpen by remember { mutableStateOf(false) }

    if (post.images.isNotEmpty()) {
        CMPager(
            images = post.images,
            state = state,
            modifier = modifier,
            onTransform = onTransform
        )
    }

    Text(
        text = buildAnnotatedString {
            val link = post.description.extractUrl()
            if (!link.isNullOrBlank()) {
                append(post.description.substringBefore(link))
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "URL",
                        styles = TextLinkStyles(
                            style = SpanStyle(color = LinkBlue)
                        ),
                        linkInteractionListener = { uriHandler.openUri(link) }
                    )
                ) {
                    append(link)
                }
                append(post.description.substringAfter(link))
            } else {
                append(post.description)
            }
        },
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = dosis,
            color = fontColor
        ),
        overflow = TextOverflow.Ellipsis,
        maxLines = if (!isOpen) 5 else Int.MAX_VALUE,
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember(::MutableInteractionSource),
                onClick = { isOpen = !isOpen }
            )
            .animateAlpha(
                delay = 0,
                duration = 250,
                condition = alpha == 1f
            )
    )
}

@Composable
private fun LikesAndTimeStamp(
    post: Post,
    token: String,
    currentUserId: String,
    currentUsername: String,
    modifier: Modifier = Modifier,
    onLikeCLick: (postId: String, token: String) -> Unit,
    onUnlikeCLick: (postId: String) -> Unit
) {
    val scope = rememberCoroutineScope()

    var isLiked by rememberSaveable {
        mutableStateOf(
            post.likes.any { it.contains(currentUserId) || it.contains(currentUsername) }
        )
    }
    var likes by rememberSaveable { mutableIntStateOf(post.likes.size) }

    Column(
        modifier = modifier
            .padding(top = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(
                    id = if (isLiked) R.drawable.like else R.drawable.unlike
                ),
                contentDescription = stringResource(id = if (isLiked) R.string.unlike else R.string.like),
                modifier = Modifier
                    .animatedLike(
                        onClick = {
                            if (isLiked) {
                                scope.launch {
                                    isLiked = false
                                    likes -= 1
                                    delay(1000L) // Delay to avoid spammy notifications
                                    onUnlikeCLick(post.toPostId())
                                }
                            } else {
                                scope.launch {
                                    isLiked = true
                                    likes += 1
                                    delay(1000L)
                                    onLikeCLick(post.toPostId(), token)
                                }
                            }
                        }
                    )
                    .padding(end = 8.dp),
                tint = if (isLiked) Red else tintColor
            )
            Text(
                text = likes.toLikes(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
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
                    color = fontColor.copy(0.6f)
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
private fun AnnouncementCardPreview() {
    AnnouncementCard(
        post = Post(
            userId = "1",
            description = "Introducing CodeLab! \uD83D\uDE80\n\n" +
                    "\n\nAfter months of hard work, I'm excited to announce the launch of CodeLab, an app designed to simplify coding project management for developers. " +
                    "Whether you're a beginner or a pro, CodeLab helps you create, track, and collaborate on coding projects effortlessly. With real-time updates, progress tracking, and an intuitive interface, this app is here to streamline your workflow. " +
                    "Stay tuned for the official release—let's code smarter, together! ✨",
            likes = listOf("uchiha_sasuke", "uzumaki_naruto"),
            images = listOf("image1", "image2")
        ),
        user = User(
            userId = "1",
            username = "pra_sidh_22",
            userType = ADMIN
        ),
        currentUserId = "1",
        currentUsername = "pra_sidh_22",
        topMembers = listOf(),
        onUsernameClick = { },
        onDeleteClick = { },
        onLikeClick = { _, _ -> },
        onUnlikeCLick = { }
    )
}