package com.mca.util.navigation

import com.mca.util.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val icon: Int = 0) {

    @Serializable
    data object Splash : Route()

    @Serializable
    data object  Login: Route()

    @Serializable
    data object ForgotPassword: Route()

    @Serializable
    data object InnerScreen: Route()

    @Serializable
    data object Home: Route(icon = R.drawable.ic_home)
}