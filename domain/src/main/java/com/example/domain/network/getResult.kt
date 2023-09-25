package com.app.domain.network

import android.util.Log
import com.example.domain.network.ApiFailure
import com.example.domain.network.map
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response


suspend fun <T> getResult(request: suspend () -> Response<T>): ApiResult<T, ApiFailure> = try {
    val response = request.invoke()
    if (response.isSuccessful) {
        ApiResult(value = response.body(), null)
    }   else {
        val error = Gson().fromJson(response.errorBody()?.charStream(), ApiError::class.java)
        error?.let { ApiResult(error = ApiFailure.ApiError(it.message)) }
            ?: throw HttpException(response)
    }
} catch (throwable: Throwable) {
    Log.e("xXx", "$throwable")
    ApiResult(error = throwable.map())
}