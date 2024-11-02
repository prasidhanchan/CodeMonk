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

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mca.ui.R
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.util.constant.Constant.ADMIN
import com.mca.util.model.Tag

/**
 * TagCard composable to display a list of tags to tag a username in a post.
 */
@Composable
fun SearchedTags(
    tags: List<Tag>?,
    modifier: Modifier = Modifier,
    onClick: (username: String) -> Unit
) {
    val visible by remember(tags) { mutableStateOf(!tags.isNullOrEmpty()) }

    Row(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .animateContentSize(animationSpec = tween(durationMillis = 600))
            .height(if (visible) 78.dp else 0.dp)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        tags?.forEach { tag ->
            TagCard(
                tag = tag,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun TagCard(
    tag: Tag,
    modifier: Modifier = Modifier,
    onClick: (username: String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(all = 6.dp)
            .height(70.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .clickable(
                indication = null,
                interactionSource = remember(::MutableInteractionSource),
                onClick = { onClick(tag.username) }
            )
            .semantics { contentDescription = tag.username },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = LightBlack,
            content = {
                AsyncImage(
                    model = tag.profileImage,
                    contentDescription = tag.username,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        )
        Row(
            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = tag.username,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = dosis,
                    color = fontColor
                ),
                modifier = Modifier.padding(start = 5.dp, bottom = 1.dp)
            )
            if (tag.isVerified || tag.userType == ADMIN) {
                Icon(
                    painter = painterResource(id = R.drawable.tick),
                    contentDescription = stringResource(id = R.string.blue_tick),
                    modifier = Modifier.scale(0.75f),
                    tint = LinkBlue
                )
            }
        }
    }
}

@Preview
@Composable
private fun TagCardPreview() {
    TagCard(
        tag = Tag(
            username = "pra_sidh_22",
            userType = ADMIN
        ),
        onClick = { }
    )
}

@Preview
@Composable
private fun SearchedTagsPreview() {
    SearchedTags(
        tags = listOf(
            Tag(
                username = "pra_sidh_22",
                userType = ADMIN
            ),
            Tag(
                username = "pra_sidh_22",
                userType = ADMIN
            )
        ),
        onClick = { }
    )
}