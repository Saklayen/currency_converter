package com.saklayen.currencyconverter.domain.currencyrate

import com.saklayen.currencyconverter.di.IoDispatcher
import com.saklayen.currencyconverter.domain.FlowUseCase
import com.saklayen.currencyconverter.domain.Result
import com.saklayen.currencyconverter.model.CurrencyRate
import com.saklayen.currencyconverter.ui.home.CurrencyRateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRateUseCase @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val currencyRaterepository: CurrencyRateRepository
) : FlowUseCase<String, CurrencyRate>(ioDispatcher) {
    override suspend fun execute(parameters: String): Flow<Result<CurrencyRate>> =  currencyRaterepository.fetchCurrencyRate()
}