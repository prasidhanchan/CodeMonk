package com.mca.auth

import com.mca.util.warpper.Response

data class UiState(
    var email: String = "",
    var password: String = "",
    var loading: Boolean = false,
    var response: Response? = null
)