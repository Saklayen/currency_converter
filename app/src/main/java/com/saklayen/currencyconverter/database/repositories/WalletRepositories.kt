package com.saklayen.currencyconverter.database.repositories

import com.saklayen.currencyconverter.database.AppDatabase
import com.saklayen.currencyconverter.database.model.Transaction
import com.saklayen.currencyconverter.database.model.Wallet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class WalletRepositories @Inject constructor(private val database: AppDatabase) {

    suspend fun getWallets(): Flow<List<Wallet>>? {
       return database.walletsDao.getWallets()
    }

    suspend fun getWalletCounts(): Flow<Int>? {
        return database.walletsDao.getWalletCounts()
    }

    suspend fun getTransactionCounts(): Flow<Int>? {
       return database.transactionDao.getTransactionsCount()
    }

    suspend fun insertTransaction(transaction: Transaction){
        database.transactionDao.insert(transaction)
    }

    suspend fun getTotalCommissions(){
        database.transactionDao.getTotalCommissions()
    }

    suspend fun insertWallets(wallet: Wallet){
        database.walletsDao.insert(wallet)
    }

    suspend fun updateWallets(wallet: Wallet){
        database.walletsDao.update(wallet)
    }

}