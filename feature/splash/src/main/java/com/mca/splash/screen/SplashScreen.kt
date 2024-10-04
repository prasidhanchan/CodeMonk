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

package com.mca.splash.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.R
import com.mca.ui.theme.Black
import com.mca.ui.theme.Yellow
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import kotlinx.coroutines.delay

@Composable
internal fun SplashScreen(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = Unit) {
        scale.animateTo(
            targetValue = 1.3f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        delay(1500L)
        if (isLoggedIn) navigateToHome() else navigateToLogin()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column(
            modifier = Modifier
                .padding(all = 15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.codemonk_logo),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .scale(scale.value)
                    .weight(2f)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosis,
                            color = fontColor
                        )
                    ) {
                        append("${stringResource(id = R.string.app_name)}\n")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = dosis,
                            color = Yellow
                        )
                    ) {
                        append(stringResource(id = R.string.dept_of_mca))
                    }
                },
                style = TextStyle(textAlign = TextAlign.Center)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen(
        isLoggedIn = false,
        navigateToLogin = { },
        navigateToHome = { }
    )
}