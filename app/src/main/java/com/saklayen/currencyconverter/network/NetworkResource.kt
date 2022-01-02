package com.saklayen.currencyconverter.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.saklayen.currencyconverter.api.ApiEmptyResponse
import com.saklayen.currencyconverter.api.ApiErrorResponse
import com.saklayen.currencyconverter.api.ApiResponse
import com.saklayen.currencyconverter.api.ApiSuccessResponse
import com.saklayen.currencyconverter.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import com.saklayen.currencyconverter.domain.Result
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
abstract class NetworkResource<RequestType>(
    @IoDispatcher val dispatcher: CoroutineDispatcher
) {
    suspend fun asFlow(): Flow<Result<RequestType>> {
        return createCall().transformLatest {
            when (it) {
                is ApiSuccessResponse -> {
                    val data = processResponse(it)
                    Timber.d("API "+data.toString()+ it.body.toString())
                    withContext(dispatcher) {
                        saveCallResult(data)
                        emit(Result.success(data))
                    }
                }
                is ApiEmptyResponse -> emit(Result.success(null))
                is ApiErrorResponse -> {
                    onFetchFailed()
                    emit(Result.error(it.errorMessage, null))
                }
            }
        }.onStart {
            emit(Result.loading(null))
        }
    }

    protected open fun onFetchFailed() {
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected open suspend fun saveCallResult(data: RequestType) {
    }

    @MainThread
    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>
}
