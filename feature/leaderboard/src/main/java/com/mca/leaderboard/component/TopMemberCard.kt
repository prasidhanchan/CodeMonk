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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.ExtraLightBlack
import com.mca.ui.theme.Green
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.util.model.User
import java.util.Locale

@Composable
internal fun TopMemberCard(
    topMember: User,
    position: Int,
    onCardClick: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(60.dp)
            .semantics { contentDescription = context.getString(R.string.top_member, position) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = String.format(
                Locale("en", "IN"), "%02d", position
            ),
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = dosis,
                color = when (position) {
                    1 -> BrandColor
                    2 -> Yellow
                    3 -> Green
                    else -> fontColor
                }
            ),
            modifier = Modifier.size(40.dp)
        )
        Surface(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxHeight()
                .clickable(
                    indication = null,
                    interactionSource = remember(::MutableInteractionSource),
                    onClick = { onCardClick(topMember.userId) }
                ),
            shape = RoundedCornerShape(10.dp),
            color = ExtraLightBlack
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = LightBlack,
                    content = {
                        AsyncImage(
                            model = topMember.profileImage.ifEmpty { R.drawable.user },
                            contentDescription = topMember.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = topMember.name,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = dosis,
                                color = fontColor
                            )
                        )
                        if (position == 1 || position == 2 || position == 3) {
                            Icon(
                                painter = painterResource(id = R.drawable.crown),
                                contentDescription = stringResource(id = R.string.crown),
                                modifier = Modifier.padding(start = 5.dp),
                                tint = Yellow
                            )
                        }
                    }
                    XPPoints(xp = topMember.xp)
                }
            }
        }
    }
}

@Composable
private fun XPPoints(
    xp: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = R.drawable.xp_icon),
            contentDescription = stringResource(id = R.string.dev_experience),
            tint = Yellow
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = stringResource(id = R.string.xp_points, xp),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = dosis,
                color = Yellow
            )
        )
    }
}

@Preview
@Composable
private fun TopMemberCardPreview() {
    TopMemberCard(
        topMember = User(
            username = "pra_sidh_22",
            name = "Prasidh Gopal Anchan",
            xp = 20
        ),
        position = 1,
        onCardClick = { }
    )
}