package com.mca.codemonk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.mca.codemonk.navigation.MainNavigation
import com.mca.ui.theme.Black
import com.mca.ui.theme.CodeMonkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Black.toArgb()))
        setContent {
            CodeMonkTheme {
                MainNavigation()
            }
        }
    }
}