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

package com.mca.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mca.ui.R
import com.mca.ui.theme.Black
import com.mca.ui.theme.BottomBarBlack
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.OffWhite
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor
import com.mca.util.constant.SnackBarHelper.Companion.showSnackBar
import com.mca.util.constant.getCurrentRoute
import com.mca.util.navigation.Route
import com.mca.util.warpper.Response
import com.mca.util.warpper.ResponseType

/**
 * BottomBar composable to display navigation screens.
 */
@Composable
fun CMBottomBar(
    visible: Boolean,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    isNewNotification: Boolean
) {
    val routes = Route.routes
    val navaBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentScreen = navaBackStackEntry?.getCurrentRoute()

    var isOpen by remember { mutableStateOf(false) }
    val animateHeight by animateDpAsState(
        targetValue = if (isOpen) 185.dp else 85.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "heightBottomBar"
    )

    LaunchedEffect(key1 = visible) {
        if (!visible) isOpen = false
    }

    val context = LocalContext.current

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 200)),
        exit = fadeOut(animationSpec = tween(durationMillis = 200)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(animateHeight + 20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    modifier = modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .height(animateHeight),
                    shape = RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    color = BottomBarBlack
                ) {
                    Row(
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .height(80.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        routes.forEach { route ->
                            if (route != Route.Post()) {
                                BottomBarItem(
                                    route = route,
                                    selected = currentScreen == route,
                                    isNewNotification = isNewNotification,
                                    onClick = {
                                        navHostController.navigate(route) {
                                            popUpTo<Route.Home>()
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            } else {
                                Spacer(modifier = Modifier.width(5.dp))
                            }
                        }
                    }
                }

                AddIcon(
                    modifier = Modifier.padding(top = 5.dp),
                    isOpen = isOpen,
                    onClick = { isOpen = !isOpen }
                )
            }

            PostOptions(
                visible = isOpen,
                onProjectOptionClick = {
                    navHostController.navigate(Route.Post()) {
                        popUpTo<Route.Home>()
                        launchSingleTop = true
                        restoreState = true
                        isOpen = false
                    }
                },
                onAnnouncementClick = {
                    showSnackBar(
                        response = Response(
                            message = context.getString(R.string.feature_not_available),
                            responseType = ResponseType.ERROR
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    route: Route,
    selected: Boolean,
    isNewNotification: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .height(55.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(
                id = if (route == Route.Notification && isNewNotification) route.notificationIcon else route.icon
            ),
            contentDescription = route.javaClass.simpleName,
            tint = if (selected) tintColor else OffWhite,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = route.javaClass.simpleName,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = if (selected) fontColor else OffWhite
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun AddIcon(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .size(45.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            ),
        tonalElevation = 10.dp,
        shape = CircleShape,
        color = BrandColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = stringResource(id = R.string.add_post),
                modifier = Modifier.rotateIcon(isOpen),
                tint = tintColor
            )
        }
    }
}

@Composable
private fun PostOptions(
    visible: Boolean,
    onProjectOptionClick: () -> Unit,
    onAnnouncementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .fillMaxWidth()
            .height(if (visible) 145.dp else 0.dp)
            .background(color = BottomBarBlack),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CMButton(
            text = stringResource(id = R.string.project_post),
            onClick = onProjectOptionClick
        )
        Spacer(modifier = Modifier.height(10.dp))
        CMButton(
            text = stringResource(id = R.string.announcement),
            textColor = Black,
            color = tintColor,
            onClick = onAnnouncementClick
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}


/**
 * Function to rotate an icon 45 degrees.
 */
private fun Modifier.rotateIcon(isOpen: Boolean) = composed {
    val rotation by animateFloatAsState(
        targetValue = if (isOpen) 45f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "rotationBottomBar"
    )
    this.rotate(rotation)
}

@Preview
@Composable
private fun PostOptionsPreview() {
    PostOptions(
        visible = true,
        onProjectOptionClick = { },
        onAnnouncementClick = { }
    )
}

@Preview
@Composable
private fun CMBottomBarPreview() {
    CMBottomBar(
        visible = true,
        isNewNotification = true,
        navHostController = rememberNavController()
    )
}