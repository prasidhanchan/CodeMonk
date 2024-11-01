/*
 * Copyright © 2024 Prasidh Gopal Anchan
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

package com.mca.auth.screen

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
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
    fun user_signUp_works() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            mainClock.advanceTimeBy(5_000) // Skip splash screen

            waitUntil(timeoutMillis = 5_000) {
                onNode(hasContentDescription(context.getString(R.string.sign_up))).isDisplayed()
            }
            // Navigate to Sign Up
            onNode(hasContentDescription(context.getString(R.string.sign_up)))
                .performTouchInput { click(percentOffset(0.9f, 0.5f)) }
            mainClock.advanceTimeBy(5_000)

            // Without name
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
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performScrollTo()
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.sign_up))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Please enter your name.").isDisplayed()
            }

            // Without username
            onNodeWithContentDescription(context.getString(R.string.name)).performTextInput("Test")
            onNodeWithContentDescription(context.getString(R.string.username)).performTextClearance()
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performScrollTo()
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.sign_up))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Please enter your username.").isDisplayed()
            }

            // Without email
            onNodeWithContentDescription(context.getString(R.string.username)).performTextInput("test_1234")
            onNodeWithContentDescription(context.getString(R.string.email_format)).performTextClearance()
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performScrollTo()
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.sign_up))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Please enter your email.").isDisplayed()
            }

            // Without password
            onNodeWithContentDescription(context.getString(R.string.email_format))
                .performTextInput("nnm23mc101@nmamit.in")
            onNodeWithContentDescription(context.getString(R.string.password))
                .performTextClearance()
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performScrollTo()
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.sign_up))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Please enter your password.").isDisplayed()
            }

            // Without re-password
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("test1234")
            onNodeWithContentDescription(context.getString(R.string.re_password)).performTextClearance()
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.sign_up))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Please re-enter your password.").isDisplayed()
            }

            // With different passwords
            onNodeWithContentDescription(context.getString(R.string.re_password)).performTextInput("test12345")
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.sign_up))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.sign_up)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Passwords do not match.").isDisplayed()
            }
        }
    }

    @Test
    fun user_login_works() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            mainClock.advanceTimeBy(5_000) // Skip splash screen

            // Login without email
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("test123")
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.login))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Please enter your email.").isDisplayed()
            }

            // Login without password
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextClearance() // Clear password
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.login))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Please enter your password.").isDisplayed()
            }

            // Login with invalid credentials
            onNodeWithContentDescription(context.getString(R.string.email)).performTextClearance() // Clear email
            onNodeWithContentDescription(context.getString(R.string.password)).performTextClearance() // Clear password
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("testing123")
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.login))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Invalid credentials.").isDisplayed()
            }

            // Clear Text
            onNodeWithContentDescription(context.getString(R.string.password)).performTextClearance()
            onNodeWithContentDescription(context.getString(R.string.email)).performTextClearance()

            // Valid login credentials
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test@gmail.com")
            onNodeWithContentDescription(context.getString(R.string.password)).performTextInput("test1234")
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.login))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.login)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Home").isDisplayed()
            }
        }
    }

    @Test
    fun user_forgotPassword_works() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            mainClock.advanceTimeBy(5_000) // Skip splash screen

            // Navigate to forgot password
            onNodeWithText(context.getString(R.string.forgot_password)).performClick()
            mainClock.advanceTimeBy(5_000)

            // Forgot password without email
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.find_account))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.find_account)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Please enter your email.").isDisplayed()
            }

            // Forgot password with invalid email
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("test")
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.find_account))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.find_account)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Enter a valid email.").isDisplayed()
            }

            // Valid email
            onNodeWithContentDescription(context.getString(R.string.email)).performTextClearance() // Clear email
            onNodeWithContentDescription(context.getString(R.string.email)).performTextInput("nnm24mc101@nmamit.in")
            waitUntil(5_000) {
                onNodeWithContentDescription(context.getString(R.string.find_account))
                    .assertIsEnabled()
                    .isDisplayed()
            }
            mainClock.advanceTimeBy(5_000)
            onNodeWithContentDescription(context.getString(R.string.find_account)).performClick()
            waitUntil(5_000) {
                onNodeWithText("Password reset link sent to your email.").isDisplayed()
            }
        }
    }
}