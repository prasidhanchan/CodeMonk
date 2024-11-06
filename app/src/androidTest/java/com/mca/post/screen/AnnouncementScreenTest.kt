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
class AnnouncementScreenTest {

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
    fun announcement_appBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.announcement)).performClick()
            mainClock.advanceTimeBy(5_000)
            waitUntil(5_000) {
                onNodeWithContentDescription("${context.getString(R.string.new_announcement)} AppBar")
                    .isDisplayed()
            }
        }
    }

    @Test
    fun bottomBar_is_not_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Announcement screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.announcement)).performClick()
            mainClock.advanceTimeBy(5_000)
            waitUntil(5_000) {
                onNodeWithContentDescription("${context.getString(R.string.new_announcement)} AppBar")
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
    fun imageSelector_textBox_and_button_are_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Announcement screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.announcement)).performClick()
            mainClock.advanceTimeBy(5_000)

            onNodeWithContentDescription(context.getString(R.string.select_your_images)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.whats_going_on)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.post)).assertIsDisplayed()
        }
    }

    @Test
    fun addAnnouncement_without_description_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Announcement screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.announcement)).performClick()
            mainClock.advanceTimeBy(5_000)

            onNodeWithContentDescription(context.getString(R.string.post)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Description cannot be empty.").isDisplayed()
            }
        }
    }

    @Test
    fun addAnnouncement_with_description_more_than_300_characters_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            // Navigate to New Announcement screen
            mainClock.advanceTimeBy(5_000) // Skip BottomBar animation
            onNodeWithContentDescription(context.getString(R.string.add_a_new_post)).performClick()
            onNodeWithContentDescription(context.getString(R.string.announcement)).performClick()
            mainClock.advanceTimeBy(5_000)

            onNodeWithContentDescription(context.getString(R.string.whats_going_on))
                .performTextInput(TestUtil.DESCRIPTION_300)
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.post)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Your description exceeds 300 characters.").isDisplayed()
            }
        }
    }
}