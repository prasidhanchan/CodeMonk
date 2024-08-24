package com.mca.util.warpper

/**
 * Wrapper class for handling response and Error
 */
data class DataOrException<T, Boolean, E: Exception>(
    val data: T? = null,
    var loading: Boolean? = null,
    val exception: E? = null
)