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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.mca.ui.theme.Black
import com.mca.ui.theme.BrandColor

/**
 * Loader composable to display a circular progress indicator when data is still loading.
 */
@Composable
fun Loader(
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = loading,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 400)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 400)
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = Black),
            contentAlignment = Alignment.Center,
            content = {
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    color = BrandColor,
                    strokeCap = StrokeCap.Round
                )
            }
        )
    }
}