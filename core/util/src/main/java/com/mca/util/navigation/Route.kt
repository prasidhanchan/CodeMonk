package com.mca.util.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val icon: Int = 0) {

    @Serializable
    data object Splash : Route()

    @Serializable
    data object  Login: Route()
}