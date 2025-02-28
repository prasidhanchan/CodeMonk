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

package com.mca.leaderboard.screen

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
class LeaderBoardScreenTest {

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
    fun leaderBoard_appBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to LeaderBoard
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.leaderboard)).performClick()
            waitUntil(5_000) {
                onNodeWithContentDescription("${context.getString(R.string.leaderboard)} AppBar")
                    .isDisplayed()
            }
        }
    }

    @Test
    fun bottomBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to LeaderBoard
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.leaderboard)).performClick()

            onNodeWithContentDescription("Home").assertIsDisplayed()
            onNodeWithContentDescription("LeaderBoard").assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.new_post)).assertIsDisplayed()
            onNodeWithContentDescription("Notification").assertIsDisplayed()
            onNodeWithContentDescription("Profile").assertIsDisplayed()
        }
    }

    @Test
    fun topMember_histograms_are_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to LeaderBoard
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.leaderboard)).performClick()

            mainClock.advanceTimeBy(5_000) // Skip data loading time
            waitUntil(6_000) {
                onNodeWithContentDescription("1").isDisplayed()
                onNodeWithContentDescription("2").isDisplayed()
                onNodeWithContentDescription("3").isDisplayed()
            }
        }
    }

    @Test
    fun topMember_cards_are_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to LeaderBoard
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.leaderboard)).performClick()

            mainClock.advanceTimeBy(5_000) // Skip data loading time
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.top_member, 1))
                    .isDisplayed()
                onNodeWithContentDescription(context.getString(R.string.top_member, 2))
                    .isDisplayed()
                onNodeWithContentDescription(context.getString(R.string.top_member, 3))
                    .isDisplayed()
            }
        }
    }
}