package com.mca.notification.screen

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.mca.codemonk.MainActivity
import com.mca.notification.navigation.sendNotificationNavigation
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
class SendNotificationScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: NotificationViewModel

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
                        startDestination = Route.SendNotification,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        sendNotificationNavigation(
                            viewModel = viewModel,
                            navHostController = navController
                        )
                    }
                }
            }
        }
    }

    @Test
    fun sendNotificationScreen_appBar_is_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription("${context.getString(R.string.send_notification)} AppBar")
                .assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.back)).assertIsDisplayed() // Back button
        }
    }

    @Test
    fun textBoxes_and_button_are_displayed() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.title)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.body)).assertIsDisplayed()
            onNodeWithContentDescription(context.getString(R.string.send)).assertIsDisplayed()
        }
    }

    @Test
    fun sendNotification_without_title_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.body)).performTextInput("Test Message")
            onNodeWithContentDescription(context.getString(R.string.send)).performClick()
            waitUntil(2_000) {
                onNodeWithContentDescription("Title cannot be empty.").isDisplayed()
            }
        }
    }

    @Test
    fun sendNotification_without_body_shows_error() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.apply {
            onNodeWithContentDescription(context.getString(R.string.title)).performTextInput("Test Title")
            onNodeWithContentDescription(context.getString(R.string.send)).performClick()
            waitUntil(2_000) {
                onNodeWithContentDescription("Message cannot be empty.").isDisplayed()
            }
        }
    }
}