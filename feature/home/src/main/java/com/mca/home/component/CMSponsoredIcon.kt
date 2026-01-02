/*
 * Copyright © 2026 Prasidh Gopal Anchan
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.R
import com.mca.ui.theme.ExtraLightBlack
import com.mca.ui.theme.dosis
import com.mca.ui.theme.tintColor

/**
 * CMSponsoredIcon composable to display sponsored icon in a Sponsored content.
 */
@Composable
fun CMSponsoredIcon(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .height(30.dp)
            .width(90.dp),
        shape = RoundedCornerShape(6.dp),
        color = ExtraLightBlack
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sponsored),
                contentDescription = stringResource(id = R.string.sponsored),
                tint = tintColor,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(
                text = stringResource(id = R.string.sponsored),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosis,
                    color = Color.White
                ),
                modifier = Modifier.padding(
                    top = 5.dp,
                    end = 10.dp,
                    bottom = 6.dp
                )
            )
        }
    }
}

@Preview
@Composable
private fun CMSponsoredIconPreview() {
    CMSponsoredIcon()
}