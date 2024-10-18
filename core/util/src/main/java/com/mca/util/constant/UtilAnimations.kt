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

package com.mca.util.constant

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Animated the scale of any composable function.
 * @param onClick The lambda called on click of the composable.
 */
fun Modifier.animatedLike(onClick: () -> Unit) = composed {
    var scale by remember { mutableFloatStateOf(1f) }
    val scope = rememberCoroutineScope()
    val animatedLike by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "animatedLike"
    )
    scale(animatedLike)
        .clickable(
            indication = null,
            interactionSource = remember(::MutableInteractionSource),
            onClick = {
                scope.launch {
                    onClick()
                    scale = 1.4f
                    delay(100L)
                    scale = 1f
                }
            }
        )
}

/**
 * Animate the alpha of any composable function such as a Card.
 * @param delay The delay of the enter animation in milliseconds.
 * @param duration The duration of the enter animation in milliseconds.
 * @param condition The condition when the composable should be shown.
 */
fun Modifier.animateAlpha(
    delay: Int,
    duration: Int = 1000,
    condition: Boolean = true
) = composed {
    var alpha by rememberSaveable { mutableFloatStateOf(0f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(
            durationMillis = duration,
            delayMillis = delay
        ),
        label = "animatedAlpha"
    )
    if (condition) {
        graphicsLayer(alpha = animatedAlpha)
            .onGloballyPositioned { alpha = 1f }
    } else {
        graphicsLayer(alpha = animatedAlpha)
            .onGloballyPositioned { alpha = 0f }
    }
}

/**
 * Function to rotate an icon 45 degrees.
 */
fun Modifier.rotateIcon(isOpen: Boolean) = composed {
    val rotation by animateFloatAsState(
        targetValue = if (isOpen) 45f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "rotationBottomBar"
    )
    rotate(rotation)
}