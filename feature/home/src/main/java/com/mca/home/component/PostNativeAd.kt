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

import android.graphics.drawable.Drawable
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.mca.ui.R
import com.mca.ui.component.CMButton
import com.mca.ui.theme.ExtraLightBlack
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.LinkBlue
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor

@Composable
internal fun PostNativeAd(nativeAd: NativeAd?, modifier: Modifier = Modifier) {
    NativeAdView(
        modifier = modifier,
        nativeAd = nativeAd
    ) { ad, composeView ->
        PostAd(
            image = ad?.icon?.uri.toString(),
            headline = ad?.headline.orEmpty(),
            body = ad?.body.orEmpty(),
            mediaImage = ad?.mediaContent?.mainImage,
            callToAction = ad?.callToAction ?: stringResource(id = R.string.install),
            onClick = { composeView.performClick() }
        )
    }
}


@Composable
private fun PostAd(
    image: String,
    headline: String,
    body: String,
    mediaImage: Drawable?,
    callToAction: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonText = "${callToAction[0]}${callToAction.substringAfter(callToAction[0]).lowercase()}"

    Column(
        modifier = modifier
            .padding(vertical = 20.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PostAdTopBar(image = image, headLine = headline)
        MainContent(
            headline = headline,
            body = body,
            mediaImage = mediaImage,
            buttonText = buttonText,
            onClick = onClick
        )
        PostAdDescription(
            description = stringResource(id = R.string.ad_description),
            username = headline
        )
    }
}

@Composable
private fun PostAdTopBar(
    image: String,
    headLine: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = image,
            contentDescription = headLine,
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(CircleShape)
                .background(LightBlack)
                .size(30.dp)
        )
        Text(
            text = headLine.ifEmpty { stringResource(id = R.string.ad_username) },
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor
            ),
            modifier = Modifier.padding(end = 5.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.tick),
            contentDescription = stringResource(id = R.string.blue_tick),
            tint = LinkBlue
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainContent(
    headline: String,
    body: String,
    mediaImage: Drawable?,
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .combinedClickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = LightBlack
    ) {
        Column(
            modifier = Modifier
                .padding(all = 15.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Surface(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .height(30.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(6.dp),
                    color = ExtraLightBlack
                ) {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.sponsored),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = dosis,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(
                                top = 5.dp,
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 6.dp
                            )
                        )
                    }
                }
                Text(
                    text = body.ifEmpty { stringResource(id = R.string.ad_body) },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosis,
                        color = fontColor
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ImageContent(mediaImage = mediaImage, headLine = headline)
            Spacer(modifier = Modifier.height(10.dp))
            CMButton(
                text = buttonText,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                fonSize = 16,
                textColor = LinkBlue,
                color = ExtraLightBlack,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun ImageContent(
    mediaImage: Drawable?,
    headLine: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(8.dp),
        color = ExtraLightBlack
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                model = mediaImage,
                contentDescription = headLine,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun PostAdDescription(
    description: String,
    username: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(fontWeight = FontWeight.ExtraBold)
                ) {
                    append(username.lowercase())
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
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
private fun NativeAdView(
    nativeAd: NativeAd?,
    modifier: Modifier = Modifier,
    content: @Composable (nativeAd: NativeAd?, view: View) -> Unit
) {
    val adViewId by remember { mutableIntStateOf(View.generateViewId()) }
    val contentViewId by remember { mutableIntStateOf(View.generateViewId()) }

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
private fun PostAdPreview() {
    PostAd(
        image = "",
        headline = "Test Ad: Google Ads",
        body = "Stay up to date with your Ads Check how your ads are performing",
        mediaImage = null,
        callToAction = "Install",
        onClick = { }
    )
}