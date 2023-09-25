package com.app.domain.network


data class ApiResult<T, E>(
    val value: T? = null,
    val error: E? = null
)
