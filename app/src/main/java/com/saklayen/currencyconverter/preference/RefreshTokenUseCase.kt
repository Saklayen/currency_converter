package com.saklayen.currencyconverter.preference

import com.saklayen.currencyconverter.di.IoDispatcher
import com.saklayen.currencyconverter.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class RefreshTokenUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<String, Unit>(dispatcher) {
    override suspend fun execute(parameters: String) {
        preferenceStorage.accessToken(parameters)
    }
}
