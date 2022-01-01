package com.saklayen.currencyconverter.api

import com.squareup.moshi.Moshi
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            Timber.d(error.toString())
            return when (error) {
                is IOException -> ApiErrorResponse("No internet connection")
                else -> ApiErrorResponse(error.message ?: "unknown error")
            }
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return when {
                response.isSuccessful -> {
                    val body = response.body()
                    when {
                        body == null || response.code() == 204 -> ApiEmptyResponse()
                        else -> ApiSuccessResponse(body = body)
                    }
                }
                else -> {
                    val msg = response.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        val moshi = Moshi.Builder().build()
                        val adapter = moshi.adapter(ApiError::class.java)
                        val apiError = adapter.fromJson(msg)
                        when {
                            apiError != null && apiError.message.isNotEmpty() -> apiError.message
                            else -> msg
                        }
                    }
                    ApiErrorResponse(errorMsg ?: "unknown error")
                }
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()
