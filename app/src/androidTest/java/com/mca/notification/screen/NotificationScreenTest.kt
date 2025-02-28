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

package com.mca.notification.screen

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.mca.codemonk.MainActivity
import com.mca.codemonk.navigation.innerScreen
import com.mca.ui.R
import com.mca.ui.theme.CodeMonkTheme
import com.mca.util.navigation.Route
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NotificationScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
            val navController = rememberNavController()

            CodeMonkTheme {
                NavHost(
                    navController = navController,
                    startDestination = Route.InnerScreen,
                    modifier = Modifier.systemBarsPadding()
                ) {
                    innerScreen(navigateToLogin = { })
                }
            }
        }
    }

    @Test
    fun notification_appBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Notification
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.notification)).performClick()

            waitUntil(5_000) {
                onNodeWithContentDescription("${context.getString(R.string.notification)} AppBar")
                    .isDisplayed()
            }
        }
    }

    @Test
    fun bottomBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Notification
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.notification)).performClick()

            onNodeWithContentDescription("Home").assertIsDisplayed()
            onNodeWithContentDescription("LeaderBoard").assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).assertIsDisplayed()
            onNodeWithContentDescription("Notification").assertIsDisplayed()
            onNodeWithContentDescription("Profile").assertIsDisplayed()
        }
    }
}