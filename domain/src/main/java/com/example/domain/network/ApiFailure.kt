package com.example.domain.network

import com.example.domain.R


sealed class ApiFailure(
    var error: String? = null,
    var errorResId: Int? = null,
    var responseCode: ResponseCode? = null
) {

    enum class ResponseCode {
        UNAUTHORIZED_ACCESS,
        FORBIDDEN,
        NOT_FOUND,
        SERVER_ERROR
    }

    data class InvalidInput(
        val errorMessageResId: Int = R.string.network_invalid_input
    ) : ApiFailure(errorResId = errorMessageResId)

    data class ApiError(
        var errorMessage: String? = null
    ) : ApiFailure(error = errorMessage)

    data class UnAuthorizedAccess(
        val errorMessageResId: Int = R.string.network_unauthorized
    ) : ApiFailure(errorResId = errorMessageResId, responseCode = ResponseCode.UNAUTHORIZED_ACCESS)

    data class Forbidden(
        val errorMessageResId: Int = R.string.network_forbidden
    ) : ApiFailure(errorResId = errorMessageResId, responseCode = ResponseCode.FORBIDDEN)

    data class NotFound(
        val errorMessageResId: Int = R.string.network_not_found
    ) : ApiFailure(errorResId = errorMessageResId, responseCode = ResponseCode.NOT_FOUND)

    data class ServerError(
        val errorMessageResId: Int = R.string.network_server_error
    ) : ApiFailure(errorResId = errorMessageResId, responseCode = ResponseCode.SERVER_ERROR)

    data class TimeOut(
        val errorMessageResId: Int = R.string.network_time_out
    ) : ApiFailure(errorResId = errorMessageResId)

    data class ConnectionError(
        val errorMessageResId: Int = R.string.network_connection_error
    ) : ApiFailure(errorResId = errorMessageResId)

    data class UnKnownError(
        val errorMessageResId: Int = R.string.network_unknown_error
    ) : ApiFailure(errorResId = errorMessageResId)
}
