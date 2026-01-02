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

package com.mca.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.ExtraLightBlack
import kotlinx.coroutines.delay

/**
 * Custom Progress Bar with animated progress.
 */
@Composable
fun CMProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = BrandColor
) {
    var postProgress by rememberSaveable { mutableFloatStateOf(0f) }
    LaunchedEffect(key1 = progress) {
        delay(800L)
        postProgress = progress
    }

    Box(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            shape = CircleShape,
            color = ExtraLightBlack,
            content = { }
        )
        Surface(
            modifier = Modifier
                .animateContentSize(animationSpec = tween(durationMillis = 800))
                .fillMaxWidth(postProgress)
                .height(10.dp),
            shape = CircleShape,
            color = color,
            content = { }
        )
    }
}

@Preview
@Composable
private fun CMProgressBarPreview() {
    CMProgressBar(progress = 0.2f)
}