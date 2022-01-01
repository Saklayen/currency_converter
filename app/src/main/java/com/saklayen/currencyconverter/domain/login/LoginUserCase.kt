package com.saklayen.currencyconverter.domain.login

import com.saklayen.currencyconverter.di.IoDispatcher
import com.saklayen.currencyconverter.domain.FlowUseCase
import com.saklayen.currencyconverter.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/*
@Singleton
class LoginUserCase @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val loginRepository: LoginRepository
) : FlowUseCase<UserLogin, User>(ioDispatcher) {
    override suspend fun execute(parameters: UserLogin): Flow<Result<User>> {
        return loginRepository.login(parameters)
    }
}*/
