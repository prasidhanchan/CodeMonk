package com.mca.home.screen

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
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
class HomeScreenTest {

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
    fun homeAppBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.my_profile)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.code_monk)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.search)).assertIsDisplayed()
        }
    }

    @Test
    fun bottomBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription("Home").assertIsDisplayed()
            onNodeWithContentDescription("LeaderBoard").assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.new_post)).assertIsDisplayed()
            onNodeWithContentDescription("Notification").assertIsDisplayed()
            onNodeWithContentDescription("Profile").assertIsDisplayed()
        }
    }

    @Test
    fun new_post_button_works() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Open
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.project_post)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.announcement)).assertIsDisplayed()

            // Close
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.project_post)).assertDoesNotExist()
            onNodeWithContentDescription(context.getString(R.string.announcement)).assertDoesNotExist()
        }
    }
}