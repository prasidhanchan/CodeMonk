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

package com.mca.search.component

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.mca.ui.R
import com.mca.ui.theme.ExtraLightBlack
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor

@Composable
internal fun ProfileCardNativeAd(
    nativeAd: NativeAd?,
    modifier: Modifier = Modifier
) {
    NativeAdView(
        nativeAd = nativeAd,
        modifier = modifier
    ) { ad, composeView ->
        SearchAd(
            headline = ad?.headline.orEmpty(),
            body = ad?.body.orEmpty(),
            icon = ad?.icon?.uri.toString(),
            callToAction = ad?.callToAction.orEmpty(),
            onClick = { composeView.performClick() }
        )
    }
}

@Composable
private fun SearchAd(
    headline: String,
    body: String,
    icon: String,
    callToAction: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(bottom = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        shape = RoundedCornerShape(10.dp),
        color = LightBlack
    ) {
        Row(
            modifier = Modifier
                .padding(all = 15.dp)
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(50.dp),
                shape = CircleShape,
                color = ExtraLightBlack,
                content = {
                    AsyncImage(
                        model = icon.ifEmpty { R.drawable.user },
                        contentDescription = headline,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.sponsored),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosis,
                            color = fontColor
                        )
                    )
                    Text(
                        text = callToAction,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosis,
                            color = LinkBlue
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (headline.isNotBlank()) {
                        Text(
                            text = headline,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = dosis,
                                color = fontColor
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(color = ExtraLightBlack)
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.tick),
                        contentDescription = stringResource(id = R.string.blue_tick),
                        tint = LinkBlue
                    )
                }
                Text(
                    text = body.ifEmpty { stringResource(id = R.string.ad_body) },
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = dosis,
                        color = fontColor.copy(alpha = 0.8f)
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NativeAdView(
    nativeAd: NativeAd?,
    modifier: Modifier = Modifier,
    content: @Composable (nativeAd: NativeAd?, view: View) -> Unit
) {
    val contentViewId by remember { mutableIntStateOf(View.generateViewId()) }
    val adViewId by remember { mutableIntStateOf(View.generateViewId()) }

    AndroidView(
        factory = { context ->
            val contentView = ComposeView(context).apply {
                id = contentViewId
            }
            NativeAdView(context).apply {
                id = adViewId
                addView(contentView)
            }
        },
        modifier = modifier,
        update = { view ->
            val adView = view.findViewById<NativeAdView>(adViewId)
            val composeView = view.findViewById<ComposeView>(contentViewId)

            if (nativeAd != null) adView.setNativeAd(nativeAd)
            adView.callToActionView = composeView
            composeView.setContent { content(nativeAd, composeView) }
        }
    )
}

@Preview
@Composable
private fun ProfileCardNativeAdPreview() {
    SearchAd(
        headline = "",
        body = "Test Ad: Google Ads",
        icon = "",
        callToAction = "Install",
        onClick = { }
    )
}