package com.asainternational.ambsmobile.model.token

data class RefreshTokenResponse(
    val refreshToken: String,
    val token: String
)