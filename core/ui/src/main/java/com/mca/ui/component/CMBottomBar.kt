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
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mca.ui.R
import com.mca.ui.theme.Black
import com.mca.ui.theme.BottomBarBlack
import com.mca.ui.theme.BrandColor
import com.mca.ui.theme.OffWhite
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor
import com.mca.util.constant.rotateIcon
import com.mca.util.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * BottomBar composable to display navigation screens.
 */
@Composable
fun CMBottomBar(
    visible: Boolean,
    navHostController: NavHostController,
    currentRoute: Route?,
    modifier: Modifier = Modifier,
    isNewNotification: Boolean
) {
    val routes = Route.routes

    val scope = rememberCoroutineScope()

    var isOpen by remember { mutableStateOf(false) }
    val animateHeight by animateDpAsState(
        targetValue = if (isOpen) 175.dp else 75.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "heightBottomBar"
    )

    LaunchedEffect(key1 = visible) {
        if (!visible) {
            scope.launch {
                delay(1000)
                isOpen = false
            }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        ),
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
                            .height(85.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        routes.forEach { route ->
                            if (route != Route.Post()) {
                                if (!isOpen) {
                                    BottomBarItem(
                                        route = route,
                                        selected = currentRoute == route,
                                        isNewNotification = isNewNotification,
                                        onClick = {
                                            if (route != currentRoute) {
                                                navHostController.navigate(route) {
                                                    popUpTo<Route.Home>()
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        }
                                    )
                                }
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
                    }

                    scope.launch {
                        delay(1000L)
                        isOpen = false
                    }
                },
                onAnnouncementClick = {
                    navHostController.navigate(Route.Announcement) {
                        popUpTo<Route.Home>()
                        launchSingleTop = true
                        restoreState = true
                    }

                    scope.launch {
                        delay(1000L)
                        isOpen = false
                    }
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
            .height(45.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(
                id = if (route == Route.Notification && isNewNotification) route.newNotificationIcon else route.icon
            ),
            contentDescription = route.javaClass.simpleName,
            tint = if (selected) tintColor else Color.Unspecified,
        )
        Text(
            text = route.javaClass.simpleName,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = if (selected) fontColor else OffWhite
            )
        )
    }
}

@Composable
private fun AddIcon(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .size(50.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            )
            .semantics { contentDescription = context.getString(R.string.add_a_new_post) },
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
                contentDescription = null,
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
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetY = { it }
        ),
        exit = ExitTransition.None
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(color = BottomBarBlack),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMButton(
                text = stringResource(id = R.string.project_post),
                modifier = Modifier.fillMaxWidth(0.8f),
                onClick = onProjectOptionClick
            )
            Spacer(modifier = Modifier.height(10.dp))
            CMButton(
                text = stringResource(id = R.string.announcement),
                modifier = Modifier.fillMaxWidth(0.8f),
                textColor = Black,
                color = tintColor,
                onClick = onAnnouncementClick
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
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
        navHostController = rememberNavController(),
        currentRoute = Route.Home
    )
}