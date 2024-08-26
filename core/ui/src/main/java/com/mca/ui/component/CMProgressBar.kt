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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {
    var postProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(key1 = Unit) {
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
            color = BrandColor,
            content = { }
        )
    }
}

@Preview
@Composable
private fun CMProgressBarPreview() {
    CMProgressBar(progress = 0.2f)
}