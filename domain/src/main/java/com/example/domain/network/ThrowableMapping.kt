package com.example.domain.network

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun Throwable.map(): ApiFailure {
    try {
        return when (this) {
            is HttpException -> when (this.code()) {
                400 -> ApiFailure.InvalidInput()
                401 -> ApiFailure.UnAuthorizedAccess()
                403 -> ApiFailure.Forbidden()
                404 -> ApiFailure.NotFound()
                500, 503 -> ApiFailure.ServerError()
                else -> ApiFailure.UnKnownError()
            }

            is SocketTimeoutException -> ApiFailure.TimeOut()
            is UnknownHostException -> ApiFailure.ConnectionError()
            is IOException -> ApiFailure.ConnectionError()
            else -> ApiFailure.UnKnownError()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ApiFailure.UnKnownError()
    }
}