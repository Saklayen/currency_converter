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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import com.saklayen.currencyconverter.domain.Result

@OptIn(ExperimentalCoroutinesApi::class)
abstract class NetworkBoundResource<ResultType, RequestType>(
    @IoDispatcher val dispatcher: CoroutineDispatcher
) {
    suspend fun asFlow(): Flow<Result<ResultType>> {
        return loadFromDb().transformLatest { dbValue ->
            if (shouldFetch(dbValue)) {
                emit(Result.loading(dbValue))
                createCall().collect { apiResponse ->
                    when (apiResponse) {
                        is ApiSuccessResponse -> withContext(dispatcher) {
                            saveCallResult(processResponse(apiResponse))
                            emitAll(loadFromDb().mapLatest { Result.success(it) })
                        }
                        is ApiEmptyResponse -> emit(Result.success(dbValue))
                        is ApiErrorResponse -> {
                            onFetchFailed()
                            emit(Result.error(apiResponse.errorMessage, dbValue))
                        }
                    }
                }

            } else {
                emit(Result.success(dbValue))
            }
        }
    }

    protected open fun onFetchFailed() {
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract suspend fun saveCallResult(data: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType?>

    @MainThread
    protected abstract fun createCall(): Flow<ApiResponse<RequestType>>
}
