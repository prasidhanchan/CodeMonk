package com.mca.profile.screen

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
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ViewProfileScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
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

    @Test
    fun viewProfile_appBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Search screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.search)).performClick()
            mainClock.advanceTimeBy(5_000)

            // Search for a user
            onNodeWithContentDescription(context.getString(R.string.search)).performTextInput("tester_cm")
            waitUntil(5_000) {
                onNodeWithContentDescription("tester_cm").isDisplayed()
            }

            // Navigate to ViewProfile Screen
            onNodeWithContentDescription("tester_cm").performClick()
            mainClock.advanceTimeBy(5_000)

            waitUntil(5_000) {
                onNodeWithContentDescription("@tester_cm AppBar")
                    .isDisplayed()
            }
        }
    }

    @Test
    fun bottomBar_is_not_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Search screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.search)).performClick()
            mainClock.advanceTimeBy(5_000)

            // Search for a user
            onNodeWithContentDescription(context.getString(R.string.search)).performTextInput("tester_cm")
            waitUntil(5_000) {
                onNodeWithContentDescription("tester_cm").isDisplayed()
            }

            // Navigate to ViewProfile Screen
            onNodeWithContentDescription("tester_cm").performClick()
            mainClock.advanceTimeBy(5_000)

            // ViewProfile Screen
            waitUntil(5_000) {
                onNodeWithContentDescription("@tester_cm AppBar").isDisplayed()
            }
            onNodeWithContentDescription("Home").assertDoesNotExist()
            onNodeWithContentDescription("LeaderBoard").assertDoesNotExist()
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).assertDoesNotExist()
            onNodeWithContentDescription("Notification").assertDoesNotExist()
            onNodeWithContentDescription("Profile").assertDoesNotExist()
        }
    }

    @Test
    fun viewProfile_components_are_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Search screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.search)).performClick()
            mainClock.advanceTimeBy(5_000)

            // Search for a user
            onNodeWithContentDescription(context.getString(R.string.search)).performTextInput("tester_cm")
            waitUntil(5_000) {
                onNodeWithContentDescription("tester_cm").isDisplayed()
            }

            // Navigate to ViewProfile Screen
            onNodeWithContentDescription("tester_cm").performClick()
            mainClock.advanceTimeBy(5_000)

            // ViewProfile Screen
            waitUntil(5_000) {
                onNodeWithContentDescription("@tester_cm AppBar").isDisplayed()
            }

            onNodeWithContentDescription("tester_cm").assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.name)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.bio_placeholder)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.currently_working_on)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.dev_experience)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.current_project_progress)).assertIsDisplayed()
        }
    }
}