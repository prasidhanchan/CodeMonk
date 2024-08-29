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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mca.ui.theme.BottomBarBlack
import com.mca.ui.theme.OffWhite
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor
import com.mca.util.constants.getCurrentRoute
import com.mca.util.navigation.Route

/**
 * BottomBar composable to display navigation screens.
 */
@Composable
fun CMBottomBar(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val routes = Route.routes
    val navaBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentScreen = navaBackStackEntry?.getCurrentRoute()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
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
                .padding(bottom = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            routes.forEach { route ->
                BottomBarItem(
                    route = route,
                    selected = currentScreen == route,
                    onClick = {
                        navHostController.navigate(route) {
                            popUpTo<Route.Home>()
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    route: Route,
    selected: Boolean,
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = route.icon),
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
    }
}

@Preview
@Composable
private fun CMBottomBarPreview() {
    CMBottomBar(navHostController = rememberNavController())
}