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
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.mca.auth.navigation.loginNavigation
import com.mca.codemonk.MainActivity
import com.mca.ui.R
import com.mca.ui.component.CMSnackBar
import com.mca.ui.theme.Black
import com.mca.ui.theme.CodeMonkTheme
import com.mca.ui.theme.Green
import com.mca.ui.theme.Red
import com.mca.util.navigation.Route
import com.mca.util.warpper.ResponseType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
            val navController = rememberNavController()
            val viewModel: AuthViewModel = hiltViewModel()
            val uiStateAuth by viewModel.uiState.collectAsState()

            CodeMonkTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        CMSnackBar(
                            message = uiStateAuth.response?.message,
                            visible = uiStateAuth.response != null,
                            iconColor = if (uiStateAuth.response?.responseType == ResponseType.ERROR) Red else Green,
                            onFinish = { viewModel.clearMessage() }
                        )
                    },
                    containerColor = Black
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.Login,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        loginNavigation(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    @Test
    fun textBoxes_and_loginButton_areVisible() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.email)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.password)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.login)).assertIsDisplayed()
        }
    }

    @Test
    fun login_without_email_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("test123")
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil {
                onNodeWithText("Please enter your email.").isDisplayed()
            }
        }
    }

    @Test
    fun login_without_password_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil {
                onNodeWithText("Please enter your password.").isDisplayed()
            }
        }
    }

    @Test
    fun login_with_invalid_email_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("test123")
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(2_000) {
                onNodeWithText("Enter a valid email.").isDisplayed()
            }
        }
    }

    @Test
    fun login_with_invalid_credentials_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("testing123")
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(2_000) {
                onNodeWithText("Invalid credentials.").isDisplayed()
            }
        }
    }

    @Test
    fun login_with_valid_credentials_shows_success_message() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("test123")
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Login Success").isDisplayed()
            }
        }
    }
}