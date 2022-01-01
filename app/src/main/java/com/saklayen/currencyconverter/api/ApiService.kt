package com.saklayen.currencyconverter.api

import com.asainternational.ambsmobile.model.token.RefreshToken
import com.asainternational.ambsmobile.model.token.RefreshTokenResponse
import com.saklayen.currencyconverter.model.CurrencyRate
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("latest")
    fun getCurrencyRates(): Flow<ApiResponse<CurrencyRate>>

    @POST("getAccessTokenByRefreshToken")
    fun getRefreshToken(@Body data: RefreshToken): Call<RefreshTokenResponse>

}
