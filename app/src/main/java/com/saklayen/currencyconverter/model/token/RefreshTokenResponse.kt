package com.saklayen.currencyconverter.model.token

data class RefreshTokenResponse(
    val refreshToken: String,
    val token: String
)