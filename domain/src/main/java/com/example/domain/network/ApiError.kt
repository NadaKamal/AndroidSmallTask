package com.app.domain.network


data class ApiError(
    val statusCode: Int,
    val message: String,
    val requestId: String,
)

