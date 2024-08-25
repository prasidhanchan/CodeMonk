package com.mca.auth.screen

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.auth.FirebaseAuth
import com.mca.codemonk.MainActivity
import com.mca.codemonk.navigation.MainNavigation
import com.mca.ui.R
import com.mca.ui.theme.CodeMonkTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AuthScreenEndToEndTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
            viewModel = hiltViewModel()

            CodeMonkTheme {
                MainNavigation()
            }
        }
    }

    @After
    fun tearDown() {
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun user_login_works() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            waitUntil(2_000) {
                onNodeWithContentDescription(context.getString(R.string.email)).isDisplayed()
                onNodeWithContentDescription(context.getString(R.string.password)).isDisplayed()
            }
            // Login without email
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("test123")
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil {
                onNodeWithText("Please enter your email.").isDisplayed()
            }

            // Login without password
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextClearance() // Clear password
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil {
                onNodeWithText("Please enter your password.").isDisplayed()
            }

            // Login with invalid credentials
            onNodeWithContentDescription(context.getString(R.string.email)).performTextClearance() // Clear email
            onNodeWithContentDescription(context.getString(R.string.password)).performTextClearance() // Clear password
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("testing123")
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(2_000) {
                onNodeWithText("Invalid credentials.").isDisplayed()
            }

            // Clear Text
            onNodeWithContentDescription(context.getString(R.string.password)).performTextClearance()
            onNodeWithContentDescription(context.getString(R.string.email)).performTextClearance()

            // Valid login credentials
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("test123")
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(2_000) {
                onNodeWithText("Login Success").isDisplayed()
            }
        }
    }

    @Test
    fun user_forgotPassword_works() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            waitUntil(2_000) {
                onNodeWithContentDescription(context.getString(R.string.email)).isDisplayed()
                onNodeWithContentDescription(context.getString(R.string.password)).isDisplayed()
            }
            // Navigate to forgot password
            onNodeWithText(context.getString(R.string.forgot_password)).performClick()
            waitUntil {
                onNodeWithContentDescription(context.getString(R.string.find_account)).isDisplayed()
            }

            // Forgot password without email
            onNodeWithContentDescription(context.getString(R.string.find_account)).performClick()
            waitUntil {
                onNodeWithText("Please enter your email.").isDisplayed()
            }

            // Forgot password with invalid email
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test")
            onNodeWithContentDescription(context.getString(R.string.find_account)).performClick()
            waitUntil(2_000) {
                onNodeWithText("Enter a valid email.").isDisplayed()
            }

            // Valid email
            onNodeWithContentDescription(context.getString(R.string.email)).performTextClearance() // Clear email
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.find_account)).performClick()
            waitUntil(2_000) {
                onNodeWithText("Password reset link sent to your email.").isDisplayed()
            }
        }
    }
}