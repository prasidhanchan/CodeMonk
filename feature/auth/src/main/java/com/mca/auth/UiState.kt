package com.mca.auth

data class UiState(
    var email: String = "",
    var password: String = "",
    var loading: Boolean = false,
    var error: String? = null
)