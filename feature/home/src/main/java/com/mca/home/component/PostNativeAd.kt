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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            callToAction = ad?.callToAction.orEmpty(),
            mediaImage = ad?.mediaContent?.mainImage,
            onClick = { composeView.performClick() }
        )
    }
}


@Composable
private fun PostAd(
    image: String,
    headline: String,
    body: String,
    callToAction: String,
    mediaImage: Drawable?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 20.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PostAdTopBar(
            image = image,
            headline = headline
        )
        MainContent(
            mediaImage = mediaImage,
            headline = headline,
            body = body,
            callToAction = callToAction,
            onClick = onClick
        )
    }
}

@Composable
private fun PostAdTopBar(
    image: String,
    headline: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = image.ifEmpty { R.drawable.user },
            contentDescription = headline,
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(CircleShape)
                .background(LightBlack)
                .size(30.dp),
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.Low
        )
        if (headline.isNotBlank()) {
            Text(
                text = headline,
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
                    .padding(end = 5.dp)
                    .fillMaxWidth(0.2f)
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(color = LightBlack)
            )
        }
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
    mediaImage: Drawable?,
    headline: String,
    body: String,
    callToAction: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .combinedClickable(onClick = onClick),
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
            AsyncImage(
                model = mediaImage,
                contentDescription = headline,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .aspectRatio(1412f / 949f),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (callToAction.isNotEmpty()) {
                val buttonText = callToAction[0].uppercase() +
                        callToAction.substringAfter(callToAction[0]).lowercase()
                CMButton(
                    text = buttonText,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .height(35.dp)
                        .fillMaxWidth(),
                    fonSize = 14,
                    color = ExtraLightBlack,
                    cornerRadius = 6.dp,
                    onClick = { }
                )
            }
            Text(
                text = body.ifEmpty { stringResource(id = R.string.ad_body) },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = dosis,
                    color = fontColor
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Row(
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CMSponsoredIcon(modifier = Modifier.padding(end = 10.dp))
                Text(
                    text = stringResource(id = R.string.ad_description),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = dosis,
                        color = fontColor
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
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
        callToAction = "Install",
        mediaImage = null,
        onClick = { }
    )
}