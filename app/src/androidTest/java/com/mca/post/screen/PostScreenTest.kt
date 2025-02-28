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

package com.mca.post.screen

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.auth.FirebaseAuth
import com.mca.TestUtil
import com.mca.codemonk.MainActivity
import com.mca.codemonk.navigation.innerScreen
import com.mca.ui.R
import com.mca.ui.component.CMSnackBar
import com.mca.ui.theme.CodeMonkTheme
import com.mca.ui.theme.Green
import com.mca.ui.theme.Red
import com.mca.util.constant.SnackBarHelper
import com.mca.util.constant.SnackBarHelper.Companion.resetMessageState
import com.mca.util.navigation.Route
import com.mca.util.warpper.ResponseType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PostScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
            FirebaseAuth.getInstance().signInWithEmailAndPassword("test@gmail.com", "test1234")
            val navController = rememberNavController()
            val snackBarResponse by SnackBarHelper.messageState.collectAsState()

            CodeMonkTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        CMSnackBar(
                            message = snackBarResponse.message,
                            visible = snackBarResponse.message?.isNotBlank() == true,
                            iconColor = if (snackBarResponse.responseType == ResponseType.ERROR) Red else Green,
                            onFinish = { resetMessageState() }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.InnerScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        innerScreen(navigateToLogin = { })
                    }
                }
            }
        }
    }

    @After
    fun tearDown() {
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun post_appBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.project_post)).performClick()
            mainClock.advanceTimeBy(5_000)
            waitUntil(5_000) {
                onNodeWithContentDescription("${context.getString(R.string.new_post)} AppBar")
                    .isDisplayed()
            }
        }
    }

    @Test
    fun bottomBar_is_not_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Post screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.project_post)).performClick()
            mainClock.advanceTimeBy(5_000)
            waitUntil(5_000) {
                onNodeWithContentDescription("${context.getString(R.string.new_post)} AppBar")
                    .isDisplayed()
            }
            onNodeWithContentDescription("Home").assertDoesNotExist()
            onNodeWithContentDescription("LeaderBoard").assertDoesNotExist()
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).assertDoesNotExist()
            onNodeWithContentDescription("Notification").assertDoesNotExist()
            onNodeWithContentDescription("Profile").assertDoesNotExist()
        }
    }

    @Test
    fun textBoxes_and_button_are_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Post screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.project_post)).performClick()
            mainClock.advanceTimeBy(5_000)

            onNodeWithContentDescription(context.getString(R.string.current_project_placeholder)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.team_member)).assertIsDisplayed()
            onNodeWithText("@me").assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.short_description)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.progress_progress)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.deadline_date))
                .performScrollTo()
                .assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.post))
                .performScrollTo()
                .assertIsDisplayed()
        }
    }

    @Test
    fun upsert_post_without_currentProject_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Post screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.project_post)).performClick()
            mainClock.advanceTimeBy(5_000)

            onNodeWithContentDescription(context.getString(R.string.team_member)).performTextInput("tester_cm")
            waitUntil(5_000) {
                onNodeWithContentDescription("tester_cm").isDisplayed()
            }
            onNodeWithContentDescription("tester_cm").performClick()
            onNodeWithContentDescription(context.getString(R.string.short_description))
                .performTextInput("This is a test description")
            onNodeWithContentDescription(context.getString(R.string.post))
                .performScrollTo()
                .performClick()

            waitUntil(5_000) {
                onNodeWithText("Current project cannot be empty.").isDisplayed()
            }
        }
    }

    @Test
    fun upsert_post_without_teamMembers_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Post screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.project_post)).performClick()
            mainClock.advanceTimeBy(5_000)

            onNodeWithContentDescription(context.getString(R.string.current_project_placeholder))
                .performTextInput("Test Project")
            onNodeWithContentDescription(context.getString(R.string.short_description))
                .performTextInput("This is a test description")
            onNodeWithContentDescription(context.getString(R.string.post))
                .performScrollTo()
                .performClick()

            waitUntil(5_000) {
                onNodeWithText("Team members cannot be empty.").isDisplayed()
            }
        }
    }

    @Test
    fun upsert_post_with_description_more_than_200_characters_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Post screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.project_post)).performClick()
            mainClock.advanceTimeBy(5_000)

            onNodeWithContentDescription(context.getString(R.string.current_project_placeholder))
                .performTextInput("Test Project")
            onNodeWithContentDescription(context.getString(R.string.team_member)).performTextInput("tester_cm")
            waitUntil(5_000) {
                onNodeWithContentDescription("tester_cm").isDisplayed()
            }
            onNodeWithContentDescription("tester_cm").performClick()
            onNodeWithContentDescription(context.getString(R.string.short_description))
                .performTextInput(TestUtil.DESCRIPTION_200)
            onNodeWithContentDescription(context.getString(R.string.post))
                .performScrollTo()
                .performClick()

            waitUntil(5_000) {
                onNodeWithText("Description cannot be more than 200 characters.").isDisplayed()
            }
        }
    }
}