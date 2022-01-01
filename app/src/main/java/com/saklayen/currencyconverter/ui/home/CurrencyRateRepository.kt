package com.saklayen.currencyconverter.ui.home

import com.saklayen.currencyconverter.utils.ControlledRunner
import com.saklayen.currencyconverter.api.ApiService
import com.saklayen.currencyconverter.di.IoDispatcher
import com.saklayen.currencyconverter.model.CurrencyRate
import com.saklayen.currencyconverter.network.NetworkResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.saklayen.currencyconverter.domain.Result
import timber.log.Timber

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRateRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    val apiService: ApiService
) {
    private val controlledRunner = ControlledRunner<Flow<Result<CurrencyRate>>>()

    suspend fun fetchCurrencyRate(): Flow<Result<CurrencyRate>> {
        Timber.d("Calling API-->")
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkResource<CurrencyRate>(dispatcher) {
                override suspend fun createCall() = apiService.getCurrencyRates()
            }.asFlow()
        }
    }
}