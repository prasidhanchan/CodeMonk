package com.mca.auth.screen

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
import com.mca.auth.navigation.signUpNavigation
import com.mca.codemonk.MainActivity
import com.mca.ui.R
import com.mca.ui.component.CMSnackBar
import com.mca.ui.theme.Black
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
class SignUpScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
            val navController = rememberNavController()
            val snackBarResponse by SnackBarHelper.messageState.collectAsState()
            viewModel = hiltViewModel()

            CodeMonkTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        CMSnackBar(
                            message = snackBarResponse.message,
                            visible = snackBarResponse.message?.isNotBlank() == true,
                            duration = 10000L,
                            iconColor = if (snackBarResponse.responseType == ResponseType.ERROR) Red else Green,
                            onFinish = { resetMessageState() }
                        )
                    },
                    containerColor = Black
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.SignUp,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        signUpNavigation(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    @Test
    fun textBoxes_and_button_and_logo_are_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.code_monk)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.name)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.username)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.email_format)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.password)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.re_password)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.sign_up)).assertIsDisplayed()
        }
    }

    @Test
    fun signUp_without_name_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("")
            onNodeWithContentDescription(context.getString(R.string.username))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.email_format))
                .performTextInput("nnm23mc101@nmamit.in")
            onNodeWithContentDescription(context.getString(R.string.password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.re_password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.sign_up))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
            waitUntil(2_000) {
                onNodeWithText("Please enter your name.").isDisplayed()
            }
        }
    }

    @Test
    fun signUp_without_username_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test")
            onNodeWithContentDescription(context.getString(R.string.username))
                .performTextInput("")
            onNodeWithContentDescription(context.getString(R.string.email_format))
                .performTextInput("nnm23mc101@nmamit.in")
            onNodeWithContentDescription(context.getString(R.string.password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.re_password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.sign_up))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
            waitUntil(2_000) {
                onNodeWithText("Please enter your username.").isDisplayed()
            }
        }
    }

    @Test
    fun signUp_without_email_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test")
            onNodeWithContentDescription(context.getString(R.string.username))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.email_format))
                .performTextInput("")
            onNodeWithContentDescription(context.getString(R.string.password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.re_password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.sign_up))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
            waitUntil(2_000) {
                onNodeWithText("Please enter your email.").isDisplayed()
            }
        }
    }

    @Test
    fun signUp_with_invalid_email_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test")
            onNodeWithContentDescription(context.getString(R.string.username))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.email_format))
                .performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.re_password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.sign_up))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
            waitUntil(2_000) {
                onNodeWithText("Enter a valid email.").isDisplayed()
            }
        }
    }

    @Test
    fun signUp_without_password_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test")
            onNodeWithContentDescription(context.getString(R.string.username))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.email_format))
                .performTextInput("nnm23mc101@nmamit.in")
            onNodeWithContentDescription(context.getString(R.string.password))
                .performTextInput("")
            onNodeWithContentDescription(context.getString(R.string.re_password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.sign_up))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
            waitUntil(2_000) {
                onNodeWithText("Please enter your password.").isDisplayed()
            }
        }
    }

    @Test
    fun signUp_without_re_password_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test")
            onNodeWithContentDescription(context.getString(R.string.username))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.email_format))
                .performTextInput("nnm23mc101@nmamit.in")
            onNodeWithContentDescription(context.getString(R.string.password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.re_password))
                .performTextInput("")
            onNodeWithContentDescription(context.getString(R.string.sign_up))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
            waitUntil(2_000) {
                onNodeWithText("Please re-enter your password.").isDisplayed()
            }
        }
    }

    @Test
    fun signUp_with_different_passwords_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.name))
                .performTextInput("Test")
            onNodeWithContentDescription(context.getString(R.string.username))
                .performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.email_format))
                .performTextInput("nnm23mc101@nmamit.in")
            onNodeWithContentDescription(context.getString(R.string.password))
                .performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.re_password))
                .performTextInput("test12345")
            onNodeWithContentDescription(context.getString(R.string.sign_up))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
            waitUntil(2_000) {
                onNodeWithText("Passwords do not match.").isDisplayed()
            }
        }
    }
}