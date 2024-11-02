package com.mca.search.screen

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
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
class SearchScreenTest {

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
    fun searchScreen_appBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Search screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.search)).performClick()
            mainClock.advanceTimeBy(5_000)

            waitUntil(5_000) {
                onNodeWithContentDescription("${context.getString(R.string.search)} AppBar")
                    .isDisplayed() &&
                onNodeWithContentDescription(context.getString(R.string.back)).isDisplayed() // Back button
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
            waitUntil(5_000) {
                onNodeWithContentDescription("${context.getString(R.string.search)} AppBar")
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
    fun searchBox_and_searchMessage_are_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Search screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.search)).performClick()
            mainClock.advanceTimeBy(5_000)
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.search)).isDisplayed() &&
                onNodeWithContentDescription(context.getString(R.string.search_something)).isDisplayed()
            }
        }
    }

    @Test
    fun search_works_as_expected() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Search screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.search)).performClick()
            mainClock.advanceTimeBy(5_000)

            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.search)).isDisplayed()
                onNodeWithContentDescription(context.getString(R.string.search_something)).isDisplayed()
            }
            onNodeWithContentDescription(context.getString(R.string.search)).performTextInput("Tester")
            waitUntil(5_000) {
                onNodeWithContentDescription("tester_cm").isDisplayed() &&
                onNodeWithContentDescription(context.getString(R.string.search_something)).isNotDisplayed()
            }
        }
    }

    @Test
    fun search_shows_not_found_message_if_user_not_found() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to Search screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.search)).performClick()
            mainClock.advanceTimeBy(5_000)

            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.search)).isDisplayed()
                onNodeWithContentDescription(context.getString(R.string.search_something)).isDisplayed()
            }
            onNodeWithContentDescription(context.getString(R.string.search)).performTextInput("Kawaki")
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.monk_not_found)).isDisplayed() &&
                onNodeWithContentDescription(context.getString(R.string.search_something)).isNotDisplayed()
            }
        }
    }
}