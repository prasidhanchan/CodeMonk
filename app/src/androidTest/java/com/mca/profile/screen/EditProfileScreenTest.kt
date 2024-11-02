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
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.mca.codemonk.MainActivity
import com.mca.profile.navigation.editProfileNavigation
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
class EditProfileScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
            viewModel = hiltViewModel()
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
                        startDestination = Route.EditProfile,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        editProfileNavigation(
                            viewModel = viewModel,
                            navHostController = navController
                        )
                    }
                }
            }
        }
    }

    @Test
    fun editProfileScreen_appBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription("${context.getString(R.string.edit_profile)} AppBar")
                .assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.back)).assertIsDisplayed() // Back button
        }
    }

    @Test
    fun updateProfile_without_username_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test User")
            onNodeWithContentDescription(context.getString(R.string.bio_placeholder))
                .performTextInput("Test Bio")
            onNodeWithContentDescription(context.getString(R.string.current_project_placeholder))
                .performTextInput("Test Project")
            onNodeWithContentDescription(context.getString(R.string.mentor_placeholder))
                .performScrollTo()
                .performTextInput("pra_sidh_22")
            waitUntil(5_000) {
                onNodeWithContentDescription("pra_sidh_22").isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription("pra_sidh_22").performClick()

            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.update)).performClick()

            waitUntil(2_000) {
                onNodeWithText("Username cannot be empty.").isDisplayed()
            }
        }
    }

    @Test
    fun updateProfile_with_invalid_username_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.username_placeholder))
                .performTextInput("Test Username")
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test User")
            onNodeWithContentDescription(context.getString(R.string.bio_placeholder))
                .performTextInput("Test Bio")
            onNodeWithContentDescription(context.getString(R.string.current_project_placeholder))
                .performTextInput("Test Project")
            onNodeWithContentDescription(context.getString(R.string.mentor_placeholder))
                .performScrollTo()
                .performTextInput("pra_sidh_22")
            waitUntil(5_000) {
                onNodeWithContentDescription("pra_sidh_22").isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription("pra_sidh_22").performClick()

            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.update)).performClick()

            waitUntil(2_000) {
                onNodeWithText("Invalid username.").isDisplayed()
            }
        }
    }

    @Test
    fun updateProfile_without_name_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.username_placeholder))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.bio_placeholder))
                .performTextInput("Test Bio")
            onNodeWithContentDescription(context.getString(R.string.current_project_placeholder))
                .performTextInput("Test Project")
            onNodeWithContentDescription(context.getString(R.string.mentor_placeholder))
                .performScrollTo()
                .performTextInput("pra_sidh_22")
            waitUntil(5_000) {
                onNodeWithContentDescription("pra_sidh_22").isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription("pra_sidh_22").performClick()

            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.update)).performClick()

            waitUntil(2_000) {
                onNodeWithText("Name cannot be empty.").isDisplayed()
            }
        }
    }

    @Test
    fun updateProfile_without_bio_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test User")
            onNodeWithContentDescription(context.getString(R.string.username_placeholder))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.current_project_placeholder))
                .performTextInput("Test Project")
            onNodeWithContentDescription(context.getString(R.string.mentor_placeholder))
                .performScrollTo()
                .performTextInput("pra_sidh_22")
            waitUntil(5_000) {
                onNodeWithContentDescription("pra_sidh_22").isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription("pra_sidh_22").performClick()

            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.update)).performClick()

            waitUntil(2_000) {
                onNodeWithText("Please add a bio.").isDisplayed()
            }
        }
    }

    @Test
    fun updateProfile_without_mentor_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test User")
            onNodeWithContentDescription(context.getString(R.string.username_placeholder))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.bio_placeholder))
                .performTextInput("This is a test bio")
            onNodeWithContentDescription(context.getString(R.string.current_project_placeholder))
                .performTextInput("Test Project")

            onNodeWithContentDescription(context.getString(R.string.update)).performScrollTo()
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.update)).performClick()

            waitUntil(2_000) {
                onNodeWithText("Please add a mentor.").isDisplayed()
            }
        }
    }
}