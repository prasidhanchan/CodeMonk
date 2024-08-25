package com.mca.util.warpper

/**
 * Response type indicating success or error for [Response] class.
 */
enum class ResponseType {
    SUCCESS,
    ERROR
}

/**
 * Response wrapper for firebase response.
 */
data class Response(
    var message: String? = null,
    var responseType: ResponseType = ResponseType.ERROR
)