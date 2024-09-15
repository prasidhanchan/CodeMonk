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

package com.mca.notification.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.util.R
import com.mca.util.constant.animateAlpha
import com.mca.util.constant.toNotificationTimeStamp
import com.mca.util.model.NotificationData

@Composable
internal fun NotificationCard(
    notification: NotificationData,
    modifier: Modifier = Modifier,
    delay: Int = 200
) {
    Surface(
        modifier = modifier
            .padding(vertical = 10.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .fillMaxWidth()
            .animateAlpha(delay),
        shape = RoundedCornerShape(10.dp),
        color = LightBlack
    ) {
        Column(
            modifier = Modifier
                .padding(all = 15.dp)
                .wrapContentHeight(Alignment.CenterVertically)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = notification.title,
                    tint = Yellow
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = notification.title,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = dosis,
                        color = fontColor
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = notification.body,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor
                ),
                modifier  = Modifier.padding(start = 2.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = notification.timeStamp.toNotificationTimeStamp(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = fontColor.copy(alpha = 0.5f)
                ),
                textAlign = TextAlign.End,
                modifier= Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun NotificationCardPreview() {
    NotificationCard(
        notification = NotificationData(
            id = "1",
            title = "New event",
            body = "There will be a hackathon this saturday, interested can register @codemonk.club",
            timeStamp = 1694092800000L
        )
    )
}