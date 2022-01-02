package com.saklayen.currencyconverter.api.interceptor

import android.content.Context
import com.saklayen.currencyconverter.api.ApiService
import com.saklayen.currencyconverter.di.ApiModule.Companion.API_URL
import com.saklayen.currencyconverter.model.token.RefreshToken
import com.saklayen.currencyconverter.preference.PreferenceStorage
import com.saklayen.currencyconverter.preference.RefreshTokenUseCase
import com.saklayen.currencyconverter.utils.BEARER_TOKEN
import com.saklayen.currencyconverter.preference.TokenUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class TokenInterceptor constructor(
    private val context: Context,
    private val storage: PreferenceStorage,
    private val tokenUseCase: TokenUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val interceptor: Interceptor
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val token1 = storage.accessToken
           // Timber.d(token1)
            val request = request(chain, token1)
            val initialResponse = chain.proceed(request)
            val code = initialResponse.code
            Timber.d(code.toString())
            when {
                code == 401 && storage.refreshToken.isNotEmpty() -> {
                    val tokenResponse = runBlocking {
                        val build = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val client = OkHttpClient.Builder()
                            .addNetworkInterceptor(interceptor).build()
                        val retrofit = Retrofit.Builder()
                            .client(client)
                            .baseUrl(API_URL)
                            .addConverterFactory(MoshiConverterFactory.create(build)).build()
                        val apiService = retrofit.create(ApiService::class.java)
                        val data = RefreshToken(token1, storage.refreshToken)
                        apiService.getRefreshToken(data).execute()
                    }

                    return when {
                        tokenResponse.code() != 200 -> {
                            logout()
                            initialResponse
                        }
                        else -> {
                            val refreshTokenResponse = tokenResponse.body()
                            return if (refreshTokenResponse == null) {
                                logout()
                                initialResponse
                            } else {
                                val token = refreshTokenResponse.token
                                val refreshToken = refreshTokenResponse.refreshToken
                                runBlocking {
                                    tokenUseCase(token)
                                    refreshTokenUseCase(refreshToken)
                                }
                                initialResponse.close()
                                val newRequest = request(chain, token)
                                chain.proceed(newRequest)
                            }
                        }
                    }
                }
                else -> return initialResponse
            }
        }
    }

    private fun request(chain: Interceptor.Chain, token: String): Request {
        val original = chain.request()
        val originalHttpUrl = original.url
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", token.BEARER_TOKEN)

            .url(originalHttpUrl)
        return requestBuilder.build()
    }

    private fun logout() {
//        val ACTION_LOGOUT = "PwdActivity.ACTION_LOGOUT"
//        val intent = Intent(ACTION_LOGOUT)
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}
